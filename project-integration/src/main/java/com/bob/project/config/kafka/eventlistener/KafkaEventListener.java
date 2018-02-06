/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.config.kafka.eventlistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @since 2017年7月5日 下午8:55:16
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class KafkaEventListener {

	/**
	 * KafkaConsumer消费速度过快,休眠一个阶段
	 */
	private static final int CONTAINER_IDLE_SLEEP_PERIOD = 5000;

	final static Logger LOGGER = LoggerFactory.getLogger(KafkaEventListener.class);

	/**
	 * 消费者长时间未poll到消息时触发的Event
	 * 
	 * @param event
	 */
	@EventListener
	@SuppressWarnings("unchecked")
	public void handlerIdleEvent(ListenerContainerIdleEvent event) {
		KafkaMessageListenerContainer<Integer, String> container = (KafkaMessageListenerContainer<Integer, String>) event.getSource();
		LOGGER.warn("容器[{}]ms时间内未能poll到消息,休眠一段5000ms之后继续", container.getContainerProperties().getIdleEventInterval());
		try {
			Thread.sleep(CONTAINER_IDLE_SLEEP_PERIOD);
		} catch (InterruptedException e) {
			LOGGER.error("容器在休眠期间被中断,重新设置中断状态");
			Thread.currentThread().interrupt();
		}
	}

}
