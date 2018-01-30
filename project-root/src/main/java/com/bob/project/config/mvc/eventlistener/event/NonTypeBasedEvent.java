package com.bob.project.config.mvc.eventlistener.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 2017年7月31日 上午9:15:10
 * @version $Id$
 * @author JiangJibo
 *
 */
public class NonTypeBasedEvent {

	final static Logger LOGGER = LoggerFactory.getLogger(NonTypeBasedEvent.class);

	public void print() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("监听普通的非继承的事件");
		}
	}

}
