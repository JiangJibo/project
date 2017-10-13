/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.eventlistener.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bob.config.mvc.eventlistener.event.TypeBasedEvent;

/**
 * 非注解式的EventListener
 * 
 * @since 2017年1月4日 下午5:27:23
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class AppContextInitializingListener implements ApplicationListener<TypeBasedEvent> {

	final static Logger LOGGER = LoggerFactory.getLogger(AppContextInitializingListener.class);

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(TypeBasedEvent event) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("AppContextInitializingListener handleEvent [ AppContextInitializedEvent ]");
		}
		event.print();
	}

}
