package com.bob.intergrate.rocket;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

/**
 * {@link RocketListener}注解解析器
 *
 * @author wb-jjb318191
 * @create 2018-03-20 9:25
 */
public class RocketListenerAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void afterSingletonsInstantiated() {

    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
