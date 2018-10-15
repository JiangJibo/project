package com.bob.common.utils.rocket.processor;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bob.common.utils.rocket.ann.RocketListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodIntrospector.MetadataLookup;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.CONFIG_PROPERTIES;
import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.CONSUMER_GROUP;
import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.CONSUME_BEAN_NAME;
import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.CONSUME_METHOD;
import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.FAILURE_HANDLER;
import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.NAMESRV_ADDR;
import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.ORDERLY;
import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.ROCKETMQ_CONSUMER_BEAN_NAME_SUFFIX;
import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.TAG;
import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.TOPIC;

/**
 * {@link RocketListener}注解解析器
 *
 * @author wb-jjb318191
 * @create 2018-03-20 9:25
 */
public class RocketListenerAnnotationPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketListenerAnnotationPostProcessor.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        String[] beanDefinitionNames = beanDefinitionRegistry.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(name);
            Class<?> beanClass;
            try {
                beanClass = resolveBeanClass(beanDefinition, beanDefinitionRegistry);
            } catch (ClassNotFoundException e) {
                throw new BeanDefinitionStoreException(String.format("解析[%s]BeanDefinition出现异常", beanDefinition.toString()), e);
            }
            Map<Method, RocketListener> annotatedMethods = introspectRocketAnnotatedMethod(beanClass);
            if (annotatedMethods.isEmpty()) {
                continue;
            }
            for (Map.Entry<Method, RocketListener> entry : annotatedMethods.entrySet()) {
                RocketListener listener = entry.getValue();
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DefaultMQPushConsumer.class);
                //让@RocketListener的定义Bean先实例化
                builder.addDependsOn(name)
                    .addPropertyValue(CONSUMER_GROUP, listener.consumerGroup())
                    .addPropertyValue(TOPIC, listener.topic())
                    .addPropertyValue(TAG, listener.tag())
                    .addPropertyValue(NAMESRV_ADDR, listener.namesrvAddr())
                    .addPropertyValue(CONSUME_BEAN_NAME, name)
                    .addPropertyValue(CONSUME_METHOD, entry.getKey())
                    .addPropertyValue(ORDERLY, listener.orderly())
                    .addPropertyValue(CONFIG_PROPERTIES, listener.configProperties())
                    .addPropertyValue(FAILURE_HANDLER, listener.failureHandler());
                //定义消费者Bean的名称格式为：factoryMethodName + RocketConsumer
                beanDefinitionRegistry.registerBeanDefinition(buildRocketConsumerBeanName(entry.getKey().getName()), builder.getBeanDefinition());
                LOGGER.info("注册[{}]标识的[{}]方法为RocketMQ Push消费者", RocketListener.class.getSimpleName(), entry.getKey().toString());
            }
        }
    }

    /**
     * 内省{@link RocketListener}标识的方法
     *
     * @param beanClass
     * @return
     */
    private Map<Method, RocketListener> introspectRocketAnnotatedMethod(Class<?> beanClass) {
        return MethodIntrospector.selectMethods(beanClass,
            (MetadataLookup<RocketListener>)(method) -> method.getAnnotation(RocketListener.class));
    }

    /**
     * 解析Bean Class对象
     *
     * @param beanDefinition
     * @param beanDefinitionRegistry
     * @return
     * @throws ClassNotFoundException
     */
    private Class<?> resolveBeanClass(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry) throws ClassNotFoundException {
        String beanClassName = beanDefinition.getBeanClassName();
        Class<?> beanClass = null;
        if (beanClassName != null) {
            beanClass = Class.forName(beanClassName);
        } else if (beanDefinition.getFactoryMethodName() != null) {
            String factoryMethodName = beanDefinition.getFactoryMethodName();
            String factoryBeanClassName = beanDefinitionRegistry.getBeanDefinition(beanDefinition.getFactoryBeanName()).getBeanClassName();
            Class<?> factoryBeanClass = Class.forName(factoryBeanClassName);
            if (ClassUtils.isCglibProxyClass(factoryBeanClass)) {
                factoryBeanClass = ClassUtils.getUserClass(factoryBeanClass);
            }
            Set<Method> candidateMethods = new HashSet<>();
            ReflectionUtils.doWithMethods(factoryBeanClass,
                candidateMethods::add,
                (method) -> method.getName().equals(factoryMethodName) && method.isAnnotationPresent(Bean.class)
            );
            // 如果同方法名称的Bean超过两个,取子类中的方法返回值
            if (candidateMethods.size() > 1) {
                Class<?> finalFactoryBeanClass = factoryBeanClass;
                Method m = candidateMethods.stream().filter(method -> method.getDeclaringClass().equals(finalFactoryBeanClass)).findFirst().get();
                beanClass = m.getReturnType();
            } else if (candidateMethods.size() == 1) {
                beanClass = candidateMethods.iterator().next().getReturnType();
            }
        }
        Assert.notNull(beanClass, String.format("解析[%s]BeanDefinition出现异常,BeanClass解析失败", beanDefinition.toString()));
        return beanClass;

    }
    /**
     * 构建{@link RocketListener}定义形式的Consumer的Bean的名称
     *
     * @param factoryMethodName
     * @return
     */
    private String buildRocketConsumerBeanName(String factoryMethodName) {
        return factoryMethodName + ROCKETMQ_CONSUMER_BEAN_NAME_SUFFIX;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}
