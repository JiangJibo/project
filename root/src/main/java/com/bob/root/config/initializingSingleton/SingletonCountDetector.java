/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.root.config.initializingSingleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @since 2017年7月26日 下午8:00:33
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class SingletonCountDetector implements SmartInitializingSingleton, BeanFactoryAware {

    final static Logger LOGGER = LoggerFactory.getLogger(SingletonCountDetector.class);

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void afterSingletonsInstantiated() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Spring容器内总共含有[{}]个SingleTon的Bean", beanFactory.getSingletonCount());
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory)beanFactory;
    }

}
