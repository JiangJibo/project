/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.root.config.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 容器初始化器,在容器开始初始化时会调用
 * 
 * @since 2017年1月9日 上午9:15:46
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ServletContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	final static Logger LOGGER = LoggerFactory.getLogger(ServletContextInitializer.class);

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)
	 */
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\t----------[{}] 容器开始初始化----------", "Servlet");
		}
	}

}
