/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.intergrate.kafka.consumer;

import java.util.Map;

import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ConsumerSeekAware;

/**
 * 当一个{@code @KafkaListener}标识的类继承此类时，可以对生成的Consumer做一定拓展
 * 
 * @since 2017年8月9日 下午5:35:46
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ConsumerSeekAwareAdapter implements ConsumerSeekAware {

	final static Logger LOGGER = LoggerFactory.getLogger(ConsumerSeekAwareAdapter.class);

	/* (non-Javadoc)
	 * @see org.springframework.kafka.listener.ConsumerSeekAware#registerSeekCallback(org.springframework.kafka.listener.ConsumerSeekAware.ConsumerSeekCallback)
	 */
	@Override
	public void registerSeekCallback(ConsumerSeekCallback callback) {

	}

	/* (non-Javadoc) 在当前Consumer被Kafka指定消费哪些Partition时触发,可以获取被指定的Partition的Topic,Partition Num,Offset
	 * @see org.springframework.kafka.listener.ConsumerSeekAware#onPartitionsAssigned(java.process.Map, org.springframework.kafka.listener.ConsumerSeekAware.ConsumerSeekCallback)
	 */
	@Override
	public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {

	}

	/* (non-Javadoc) 在当前Consumer空闲时触发,可以指定此Consumer消费另外的分区,前提是 {@code @KafkaListener}注解指定了Partition,而不是由Kafka来指定消费的分区
	 * @see org.springframework.kafka.listener.ConsumerSeekAware#onIdleContainer(java.process.Map, org.springframework.kafka.listener.ConsumerSeekAware.ConsumerSeekCallback)
	 */
	@Override
	public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
		for (Map.Entry<TopicPartition, Long> entry : assignments.entrySet()) {
			String topic = entry.getKey().topic();
			LOGGER.debug("空闲Consumer Topic:[{}],Partition:[{}],Offset:[{}]", topic, entry.getKey().partition(), entry.getValue());
			if ("bob".equals(topic)) {
				LOGGER.debug("指定空闲Consumer去消费 Topic:[{}],Partition:[{}],Offset:[{}]", topic, entry.getKey().partition(), entry.getValue());
				callback.seek(topic, entry.getKey().partition(), entry.getValue());
			}
		}
	}

}
