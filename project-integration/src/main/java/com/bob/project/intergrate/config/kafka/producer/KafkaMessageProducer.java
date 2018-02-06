/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.intergrate.config.kafka.producer;

import java.util.concurrent.Callable;

import com.bob.project.intergrate.config.kafka.entity.KafkaMessageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * @since 2017年7月6日 下午4:41:03
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class KafkaMessageProducer {

	final static Logger LOGGER = LoggerFactory.getLogger(KafkaMessageProducer.class);

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	private int num = 0;

	private final Gson gson = new Gson();

	/**
	 * 定期的生产消息
	 */
	@Scheduled(initialDelay = 5000, fixedDelay = 30000)
	public void regularProduceMessage() {
		prodceMessage();
	}

	/**
	 * 生产消息
	 */
	private void prodceMessage() {
		LOGGER.debug("*******************************************");
		Callable<String> callable = () -> {
			KafkaMessageEntity entity = new KafkaMessageEntity();
			entity.setName("Lucy");
			entity.setSex("女");
			for (int i = 30 * num; i < 30 * (num + 2); i++) {
				entity.setId(i);
				kafkaTemplate.sendDefault(i, gson.toJson(entity));
			}
			num = num + 2;
			return "success";
		};
		threadPoolTaskExecutor.submitListenable(callable);
	}

}
