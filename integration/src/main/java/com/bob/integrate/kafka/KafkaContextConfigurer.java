package com.bob.integrate.kafka;

import com.bob.integrate.kafka.factoryconfig.KafkaContainerFactoryConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.AbstractKafkaListenerContainerFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AbstractMessageListenerContainer.AckMode;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Kafka消息队列配置
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2017年6月28日 上午9:49:23
 */
@EnableKafka
@Configuration
@ComponentScan
@PropertySource("classpath:kafka-config.properties")
public class KafkaContextConfigurer {

    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private KafkaContainerFactoryConfigurer kafkaContainerFactoryConfigurer;

    /**
     * KafkaProducer的使用模板,线程安全的,但实际使用的都是KafkaProducerFactory创建的同一个KafkaProducer
     * 因此想要实现发送不同的消息需要定义多个的KafkaTemplate
     *
     * @return
     */
    @Bean
    public KafkaTemplate<Integer, String> defaultKafkaTemplate() {
        KafkaTemplate<Integer, String> kafkaTemp = new KafkaTemplate<Integer, String>(kafkaContainerFactoryConfigurer.getKafkaProducerFactory(), true);
        kafkaTemp.setDefaultTopic(kafkaTopic);
        return kafkaTemp;
    }

    /**
     * 自定义一个KafkaListenerContainerFactory,负责生产BatchListener
     *
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, String> loggingListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> cf = new ConcurrentKafkaListenerContainerFactory<Integer, String>();
        cf.setConsumerFactory(kafkaContainerFactoryConfigurer.getLoggingKafkaConsumerFactory());
        cf.setBatchListener(true);
        ContainerProperties cp = cf.getContainerProperties();
        cp.setConsumerTaskExecutor(threadPoolTaskExecutor);
        cp.setListenerTaskExecutor(threadPoolTaskExecutor);
        cp.setAckMode(AckMode.COUNT); // 按数量来commit
        cp.setAckCount(200); // 设定当满200条时commit一次
        cp.setPollTimeout(3000);
        cp.setIdleEventInterval(5000L);
        return cf;
    }

    /**
     * ContainerFactory的ContainerProperties的很多配置均会覆盖拷贝到每一个其生成的Container中 配置ContainerProperties,
     *
     * {@linkplain AbstractKafkaListenerContainerFactory#initializeContainer }
     *
     * 定义在ContainerProperties内的属性会拷贝覆盖其生成的每个Container的
     * "topics","topicPartitions","topicPattern","messageListener"之外的 所有属性(
     * 即使ContainerProperties内的属性是null );
     * 即同一个ContainerFactory的生成的所有Container的ContainerProperties,只有这几个属性不同,其他属性都相同,
     * 均为ContainerFactory的配置的ContainerProperties
     *
     * {@linkplain AbstractKafkaListenerContainerFactory#createListenerContainer(KafkaListenerEndpoint endpoint)}
     *
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> cf = new ConcurrentKafkaListenerContainerFactory<Integer, String>();
        cf.setConsumerFactory(kafkaContainerFactoryConfigurer.getDefaultKafkaConsumerFactory());
        // 是否设置并发因子,即此容器工厂针对{@code @KafkaListener}标识的方法(用topics属性指定Tpoic)生成多个Consumer
        cf.setConcurrency(2);

        // 当@KafkaListener标识的方法的消息参数是Message<?>时，可以通过设置MessageConveter为StringJsonMessageConverter实现
        // cf.setMessageConverter(new StringJsonMessageConverter());
        // 是否将容器内所有的MessageListener定义成BatchMessagingMessageListenerAdapter(批量消费者,默认否),默认的Listener是RecordMessagingMessageListenerAdapter(单个消费者)
        ContainerProperties cp = cf.getContainerProperties();
        cp.setConsumerTaskExecutor(threadPoolTaskExecutor);
        cp.setListenerTaskExecutor(threadPoolTaskExecutor);
        // cp.setAckMode(AckMode.MANUAL); // 手动对Ack的确认
        cp.setPollTimeout(3000); // 设置轮询消息的最长等待时间
        cp.setIdleEventInterval(5000L); // 设置MessageListenerContainer空闲多长触发IdleEvent
        return cf;
    }

}
