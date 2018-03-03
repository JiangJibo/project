package com.bob.web.config.stringvalueresolver;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringValueResolver;

/**
 * @author Administrator
 * @create 2018-03-03 9:31
 */
public class StringValueResolverRegister extends InstantiationAwareBeanPostProcessorAdapter {

    @Autowired
    private DefaultListableBeanFactory beanFactory;

    private AtomicBoolean initLock = new AtomicBoolean(false);

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanFactory.isConfigurationFrozen() && initLock.compareAndSet(false, true)) {
            beanFactory.getBean(CustomizedStringValueResolver.class);
        }
        return super.postProcessBeforeInstantiation(beanClass, beanName);
    }
}
