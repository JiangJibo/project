/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.intergrate.kafka.producer;

import java.util.Map;

import com.bob.intergrate.kafka.consumer.KafkaConsumerProcessor;
import com.bob.intergrate.kafka.constant.KafkaContextConstant;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 通过Key计算存入哪个分区
 * 
 * @since 2017年7月6日 下午8:41:41
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class PartitionSelecter implements Partitioner {

	final static Logger LOGGER = LoggerFactory.getLogger(PartitionSelecter.class);

	private static volatile Integer partition_compute_radix = 2; // 分区计算的基数

	@Autowired
	private KafkaConsumerProcessor kafkaConsumerProcessor;

	/**
	 * 定期的获取分区计算基数(Partition个数),若新的基数值大于当前值,则将作为新的基数值
	 */
	@Scheduled(fixedDelay = 30 * 1000)
	private void retrieveConfiguredRadix() {
		int radix = PartitionerRadixProcessor.retrieveRadixFromProperties();
		if (radix <= partition_compute_radix) {
			return;
		}
		LOGGER.info("重新指定了分区计算的基数,从原来值[{}]增加为[{}]", partition_compute_radix, radix);
		kafkaConsumerProcessor.generateKafkaConsumer(KafkaContextConstant.KAFKA_TOPIC, radix - partition_compute_radix);
		partition_compute_radix = radix;
	}

	/* (non-Javadoc)
	 * @see org.apache.kafka.common.Configurable#configure(java.util.Map)
	 */
	@Override
	public void configure(Map<String, ?> configs) {

	}

	/* (non-Javadoc)
	 * @see org.apache.kafka.clients.producer.Partitioner#partition(java.lang.String, java.lang.Object, byte[], java.lang.Object, byte[], org.apache.kafka.common.Cluster)
	 */
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

		return ((Integer) key) % partition_compute_radix;
	}

	/* (non-Javadoc)
	 * @see org.apache.kafka.clients.producer.Partitioner#close()
	 */
	@Override
	public void close() {
		LOGGER.info("当前分区计算器关闭");
	}

}
