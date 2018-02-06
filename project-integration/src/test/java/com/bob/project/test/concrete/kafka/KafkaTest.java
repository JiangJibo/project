/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.kafka;

import com.bob.project.intergrate.config.kafka.constant.KafkaContextConstant;
import com.bob.project.intergrate.config.kafka.entity.KafkaMessageEntity;
import com.bob.project.test.config.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import com.google.gson.Gson;

/**
 * Kafka消息队列测试
 * 
 * @since 2017年6月28日 上午10:30:18
 * @version $Id$
 * @author JiangJibo
 *
 */
public class KafkaTest extends BaseControllerTest {

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;


	/**
	 * 向kafka里写数据.<br/>
	 * 
	 */
	@Test
	public void testTemplateSend() {
		KafkaMessageEntity entity = new KafkaMessageEntity();
		entity.setName("Lucy");
		entity.setSex("女");
		Gson gson = new Gson();
		for (int i = 0; i < 20; i++) {
			entity.setId(i);
			kafkaTemplate.sendDefault(i, gson.toJson(entity));
		}
	}

	/**
	 * 测试发送的消息超出了Partition范围会如何,结果:
	 * 
	 * org.apache.kafka.common.KafkaException: Invalid partition given with record: 6 is not in the
	 * range [0...4). at
	 * org.apache.kafka.clients.producer.KafkaProducer.waitOnMetadata(KafkaProducer.java:564) at
	 * org.apache.kafka.clients.producer.KafkaProducer.doSend(KafkaProducer.java:450) at
	 * org.apache.kafka.clients.producer.KafkaProducer.send(KafkaProducer.java:440) at
	 * org.springframework.kafka.core.DefaultKafkaProducerFactory$CloseSafeProducer.send(DefaultKafkaProducerFactory.java:156) at
	 * org.springframework.kafka.core.KafkaTemplate.doSend(KafkaTemplate.java:241) at
	 * org.springframework.kafka.core.KafkaTemplate.send(KafkaTemplate.java:163) at
	 * KafkaTest.testSendOutofPartition(KafkaTest.java:62) at
	 */
	@Test
	public void testSendOutofPartition() {
		KafkaMessageEntity entity = new KafkaMessageEntity();
		entity.setName("Lucy");
		entity.setSex("女");
		Gson gson = new Gson();
		kafkaTemplate.send(KafkaContextConstant.KAFKA_TOPIC, 6, gson.toJson(entity));
	}

}
