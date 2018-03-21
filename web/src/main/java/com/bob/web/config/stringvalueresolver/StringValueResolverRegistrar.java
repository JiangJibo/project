package com.bob.web.config.stringvalueresolver;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 自定义StringValueResolver注册器
 *
 * @author Administrator
 * @create 2018-03-03 9:31
 */
public class StringValueResolverRegistrar extends InstantiationAwareBeanPostProcessorAdapter {

    @Autowired
    private DefaultListableBeanFactory beanFactory;

    private AtomicBoolean registerLock = new AtomicBoolean(false);

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanFactory.isConfigurationFrozen() && registerLock.compareAndSet(false, true)) {
            beanFactory.getBean(CustomizedStringValueResolver.class);
        }
        return super.postProcessBeforeInstantiation(beanClass, beanName);
    }
}
