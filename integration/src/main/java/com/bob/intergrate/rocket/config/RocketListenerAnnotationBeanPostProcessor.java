package com.bob.intergrate.rocket.config;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.bob.intergrate.rocket.ann.RocketListener;
import com.bob.intergrate.rocket.listener.RocketMessageListenerEndpoint;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.StringUtils;

import static com.bob.intergrate.rocket.constant.RocketDefinitionConstant.CONSUMER_GROUP;
import static com.bob.intergrate.rocket.constant.RocketDefinitionConstant.CONSUME_BEAN;
import static com.bob.intergrate.rocket.constant.RocketDefinitionConstant.CONSUME_METHOD;
import static com.bob.intergrate.rocket.constant.RocketDefinitionConstant.NAMESRV_ADDR;
import static com.bob.intergrate.rocket.constant.RocketDefinitionConstant.TAG;
import static com.bob.intergrate.rocket.constant.RocketDefinitionConstant.TOPIC;

/**
 * @author wb-jjb318191
 * @create 2018-03-20 14:01
 */
public class RocketListenerAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketListenerAnnotationBeanPostProcessor.class);

    @Autowired
    private BeanFactory beanFactory;

    /**
     * 提取{@link RocketListener}里定义的属性至RocketMQ消费者
     *
     * @see RocketListener
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultMQPushConsumer) {
            DefaultMQPushConsumer consumer = (DefaultMQPushConsumer)bean;
            consumer.setConsumerGroup(getPropertyValue(pvs, CONSUMER_GROUP));
            String namesrvAddr = getPropertyValue(pvs, NAMESRV_ADDR);
            if (StringUtils.hasText(namesrvAddr)) {
                consumer.setNamesrvAddr(namesrvAddr);
            }
            consumer.setVipChannelEnabled(false);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //订阅
            String topic = getPropertyValue(pvs, TOPIC);
            String tag = getPropertyValue(pvs, TAG);
            try {
                consumer.subscribe(topic, tag);
            } catch (MQClientException e) {
                throw new BeanCreationException(
                    String.format("订阅基于Topic:[%s],Tag:[%s]的RocketMQ消费者创建失败", topic, tag));
            }
            Object consumeBean = beanFactory.getBean(getPropertyValue(pvs, CONSUME_BEAN));
            Method consumeMethod = getConsumeMethod(pvs);
            if (AopUtils.isAopProxy(consumeBean)) {
                consumeMethod = MethodIntrospector.selectInvocableMethod(getConsumeMethod(pvs), consumeBean.getClass());
            }
            consumer.registerMessageListener(new RocketMessageListenerEndpoint(consumeBean, consumeMethod));
            LOGGER.info("订阅基于ConsumeGroup:[{}],Topic:[{}],Tag:[{}]的RocketMQ消费者创建成功", consumer.getConsumerGroup(), topic, tag);
            return null;
        }
        return super.postProcessPropertyValues(pvs, pds, bean, beanName);
    }

    /**
     * @param pvs
     * @param propertyName
     * @return
     */
    private String getPropertyValue(PropertyValues pvs, String propertyName) {
        return (String)pvs.getPropertyValue(propertyName).getValue();
    }

    /**
     * @param pvs
     * @return
     */
    private Method getConsumeMethod(PropertyValues pvs) {
        return (Method)pvs.getPropertyValue(CONSUME_METHOD).getValue();
    }
}
