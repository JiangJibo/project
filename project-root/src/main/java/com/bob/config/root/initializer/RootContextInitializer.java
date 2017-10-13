/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.root.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * 容器初始化器,在容器开始初始化时会调用
 * 
 * @since 2017年1月4日 下午5:18:15
 * @version $Id$
 * @author JiangJibo
 *
 */
@Order(1)
public class RootContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	final static Logger LOGGER = LoggerFactory.getLogger(RootContextInitializer.class);

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)
	 */
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\t----------[ ApplicationContext ] 容器开始初始化----------");
		}
	}

}
