/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.config.mvc.initializingSingleton;

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
public class SingleTonCountDetecter implements SmartInitializingSingleton, BeanFactoryAware {

	final static Logger LOGGER = LoggerFactory.getLogger(SingleTonCountDetecter.class);

	private DefaultListableBeanFactory beanFactory;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.SmartInitializingSingleton#afterSingletonsInstantiated()
	 */
	@Override
	public void afterSingletonsInstantiated() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Spring容器内总共含有[{}]个SingleTon的Bean", beanFactory.getSingletonCount());
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

}
