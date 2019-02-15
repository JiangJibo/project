/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.integrate.kafka.consumer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.bob.integrate.kafka.factoryconfig.KafkaContainerFactoryConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.adapter.HandlerAdapter;
import org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.GenericMessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.HeaderMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.HeadersMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 测试Kafka Consumer中止及重新启动后Kafka的处理策略
 * 
 * @since 2017年8月11日 下午8:53:09
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class KafkaConsumerProcessor {

	final static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerProcessor.class);

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private KafkaListenerEndpointRegistry defaultKafkaListenerEndpointRegistry;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private AnnotatedMethodKafkaConsumer kafkaConsumerBean;

	@Autowired
	private KafkaContainerFactoryConfigurer kafkaContainerFactoryConfigurer;


	/**
	 * 在一定时间内销毁指定的Kafka Consumer,此消费者对应的Partition不会被分配给其他的忙碌中的消费者,若有新的消费者则可消费此Partition
	 * 
	 * 若这个ListenerContainer对象是{@code ConcurrentMessageListenerContainer},则其调用stop()方法时,
	 * 会将停止由@KafkaListener标识的方法及concurrency基数生成的所有Consumer,若concurrency>1,则会stop多个Consumer
	 * 
	 * @see ConcurrentMessageListenerContainer
	 */
	// @Scheduled(initialDelay = 30000, fixedDelay = 1000*3600*12)
	public void destoryKafkaListenerContainer() {
		MessageListenerContainer container = defaultKafkaListenerEndpointRegistry.getListenerContainer("annotatedMethodKafkaConsumer#0");
		if (container == null || !container.isRunning()) {
			return;
		}
		LOGGER.info("Spring容器在启动60秒之后销毁ID:[{}] KafkaListenerContainer", "annotatedMethodKafkaConsumer#0");
		container.stop();
	}

	/**
	 * 重新恢复被销毁的Kafka Consumer,新加入的消费者会被Kafka指定消费一个空闲Partition
	 * 
	 * 若这个ListenerContainer对象是{@code ConcurrentMessageListenerContainer},则其调用start()方法时, 会生成由
	 * concurrency个Consumer,前提是{@code @KafkaListener}注解通过topics指定Topic
	 * 
	 * @see ConcurrentMessageListenerContainer#doStart()
	 */
	// @Scheduled(initialDelay = 45000, fixedDelay = 1000*3600*12)
	public void restartKafkaListenerContainer() {
		MessageListenerContainer container = defaultKafkaListenerEndpointRegistry.getListenerContainer("annotatedMethodKafkaConsumer#1");
		if (container == null || container.isRunning()) {
			return;
		}
		LOGGER.info("Spring重新启动KafkaListenerContainer:[{}] ", "annotatedMethodKafkaConsumer#1");
		container.start();
	}

	/**
	 * 生成新的Kafka Consumer
	 * 
	 * @param topic
	 *            消息的主题
	 * @param num
	 *            新生成消费者的数量
	 */
	public void generateKafkaConsumer(String topic, int num) {
		Assert.isTrue(num > 0, "新增的Kafka Consumer个数不能小于1");
		ConsumerFactory<Integer, String> cf = kafkaContainerFactoryConfigurer.getDefaultKafkaConsumerFactory();
		ContainerProperties cp = new ContainerProperties(topic);
		cp.setConsumerTaskExecutor(threadPoolTaskExecutor);
		cp.setListenerTaskExecutor(threadPoolTaskExecutor);
		cp.setPollTimeout(3000);
		cp.setIdleEventInterval(5000L);
		Method consumerMethod = ReflectionUtils.findMethod(AnnotatedMethodKafkaConsumer.class, "consume1", Message.class);
		RecordMessagingMessageListenerAdapter<Integer, String> listenerAdapter = new RecordMessagingMessageListenerAdapter<Integer, String>(kafkaConsumerBean,
				consumerMethod);
		listenerAdapter.setMessageConverter(new StringJsonMessageConverter());
		HandlerAdapter handlerAdapter = new HandlerAdapter(
				createDefaultMessageHandlerMethodFactory().createInvocableHandlerMethod(kafkaConsumerBean, consumerMethod));
		listenerAdapter.setHandlerMethod(handlerAdapter);
		cp.setMessageListener(listenerAdapter);
		for (int i = 0; i < num; i++) {
			MessageListenerContainer container = new KafkaMessageListenerContainer<Integer, String>(cf, cp);
			container.start();
		}
		LOGGER.info("针对Topic:[{}]新生成[{}]个消费者", topic, num);
	}

	private MessageHandlerMethodFactory createDefaultMessageHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory defaultFactory = new DefaultMessageHandlerMethodFactory();
		defaultFactory.setBeanFactory(beanFactory);

		ConfigurableBeanFactory cbf = (beanFactory instanceof ConfigurableBeanFactory ? (ConfigurableBeanFactory) beanFactory : null);

		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		defaultFactory.setConversionService(conversionService);

		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

		// Annotation-based argument resolution
		argumentResolvers.add(new HeaderMethodArgumentResolver(conversionService, cbf));
		argumentResolvers.add(new HeadersMethodArgumentResolver());

		// Type-based argument resolution
		final GenericMessageConverter messageConverter = new GenericMessageConverter(conversionService);
		argumentResolvers.add(new MessageMethodArgumentResolver(messageConverter));
		argumentResolvers.add(new PayloadArgumentResolver(messageConverter) {

			@Override
			protected boolean isEmptyPayload(Object payload) {
				return payload == null || payload instanceof KafkaNull;
			}

		});
		defaultFactory.setArgumentResolvers(argumentResolvers);

		defaultFactory.afterPropertiesSet();
		return defaultFactory;
	}

}
