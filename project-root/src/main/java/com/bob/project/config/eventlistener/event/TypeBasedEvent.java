package com.bob.project.config.eventlistener.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * @since 2017年7月31日 上午9:15:42
 * @version $Id$
 * @author JiangJibo
 *
 */
public class TypeBasedEvent extends ApplicationEvent {

	private static final long serialVersionUID = -7561589459556542709L;

	final static Logger LOGGER = LoggerFactory.getLogger(TypeBasedEvent.class);

	public TypeBasedEvent(ApplicationContext context) {
		super(context);
	}

	public void print() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("监听类型继承的事件,[{}]", ((ApplicationContext) getSource()).getClass());
		}
	}

}
