package com.bob.intergrate.rocket.integrate.processor;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.bob.intergrate.rocket.integrate.ann.RocketListener;
import com.bob.intergrate.rocket.integrate.listener.RocketMessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import static com.bob.intergrate.rocket.integrate.constant.RocketBeanDefinitionConstant.CONFIG_PROPERTIES;
import static com.bob.intergrate.rocket.integrate.constant.RocketBeanDefinitionConstant.CONSUMER_GROUP;
import static com.bob.intergrate.rocket.integrate.constant.RocketBeanDefinitionConstant.CONSUME_BEAN_NAME;
import static com.bob.intergrate.rocket.integrate.constant.RocketBeanDefinitionConstant.CONSUME_FROM_WHERE;
import static com.bob.intergrate.rocket.integrate.constant.RocketBeanDefinitionConstant.CONSUME_METHOD;
import static com.bob.intergrate.rocket.integrate.constant.RocketBeanDefinitionConstant.NAMESRV_ADDR;
import static com.bob.intergrate.rocket.integrate.constant.RocketBeanDefinitionConstant.ORDERLY;
import static com.bob.intergrate.rocket.integrate.constant.RocketBeanDefinitionConstant.TAG;
import static com.bob.intergrate.rocket.integrate.constant.RocketBeanDefinitionConstant.TOPIC;

/**
 * RocketMQ Bean处理器
 *
 * @author wb-jjb318191
 * @create 2018-03-20 14:01
 */
public class RocketListenerAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketListenerAnnotationBeanPostProcessor.class);

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    private List<String> excludeProperties = Arrays.asList("topic", "tag");

    /**
     * 初始化消费者属性
     *
     * @see RocketListener
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultMQPushConsumer) {
            DefaultMQPushConsumer consumer = (DefaultMQPushConsumer)bean;
            //若配置了了Properties文件,首先用配置文件的数据初始化Bean
            String properties = getStringProperty(pvs, CONFIG_PROPERTIES);
            if (StringUtils.hasText(properties)) {
                initBeanByProperties(bean, properties, excludeProperties);
            }
            String consumeGroup = getStringProperty(pvs, CONSUMER_GROUP);
            if (StringUtils.hasText(consumeGroup)) {
                consumer.setConsumerGroup(consumeGroup);
            }
            String namesrvAddr = getStringProperty(pvs, NAMESRV_ADDR);
            if (StringUtils.hasText(namesrvAddr)) {
                consumer.setNamesrvAddr(namesrvAddr);
            }
            consumer.setConsumeFromWhere(getProperty(pvs, CONSUME_FROM_WHERE, ConsumeFromWhere.class));
            //订阅信息
            String topic = getStringProperty(pvs, TOPIC);
            if (!StringUtils.hasText(topic)) {
                topic = loadProperties(properties).getProperty(TOPIC);
            }
            String tag = getStringProperty(pvs, TAG);
            if(!StringUtils.hasText(tag)){
                tag = loadProperties(properties).getProperty(TAG);
            }
            try {
                consumer.subscribe(topic, tag);
            } catch (MQClientException e) {
                throw new BeanCreationException(String.format("订阅基于Topic:[%s],Tag:[%s]的RocketMQ消费者创建失败", topic, tag));
            }
            Object consumeBean = beanFactory.getBean(getStringProperty(pvs, CONSUME_BEAN_NAME));
            Method consumeMethod = getProperty(pvs, CONSUME_METHOD, Method.class);
            if (ClassUtils.isCglibProxy(consumeBean)) {
                consumeMethod = MethodIntrospector.selectInvocableMethod(consumeMethod, consumeBean.getClass());
            }
            //是否有序
            boolean ordered = getProperty(pvs, ORDERLY, boolean.class);
            if (ordered) {
                consumer.registerMessageListener((MessageListenerOrderly)new RocketMessageListener(consumeBean, consumeMethod));
            } else {
                consumer.registerMessageListener((MessageListenerConcurrently)new RocketMessageListener(consumeBean, consumeMethod));
            }
            LOGGER.info("订阅基于ConsumeGroup:[{}],Topic:[{}],Tag:[{}]的RocketMQ消费者创建成功", consumer.getConsumerGroup(), topic, tag);
            return null;
        }
        return super.postProcessPropertyValues(pvs, pds, bean, beanName);
    }

    /**
     * 通过Properties文件初始化Bean对象属性
     *
     * @param bean
     * @param propertiesPath
     * @param excludePorperties
     */
    private void initBeanByProperties(Object bean, String propertiesPath, List<String> excludePorperties) {
        Properties properties = loadProperties(propertiesPath);
        BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
        for (String key : properties.stringPropertyNames()) {
            if (!excludePorperties.contains(key)) {
                beanWrapper.setPropertyValue(key, properties.get(key));
            }
        }
    }

    /**
     * @param filePath
     * @return
     */
    private Properties loadProperties(String filePath) {
        Resource resource = new ClassPathResource(filePath);
        Assert.isTrue(resource.exists(), String.format("配置文件:[%s]不存在", filePath));
        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw new BeanCreationException(String.format("加载RocketMQ Consumer配置文件[%s]出现异常", filePath), e);
        }
        return properties;
    }

    /**
     * 解析配置,如果配置以"$"开始,则从Properties文件中查找相应的值
     *
     * @param pvs
     * @param propertyName
     * @return
     */
    private String getStringProperty(PropertyValues pvs, String propertyName) {
        String value = (String)pvs.getPropertyValue(propertyName).getValue();
        if (value.startsWith("$")) {
            value = beanFactory.resolveEmbeddedValue(value);
        }
        return value;
    }

    /**
     * @param pvs
     * @return
     */
    private <T> T getProperty(PropertyValues pvs, String name, Class<T> targetClass) {
        return (T)pvs.getPropertyValue(name).getValue();
    }
}
