/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.config.mvc.kafka.factoryconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import com.bob.project.config.mvc.kafka.constant.KafkaContextConstant;
import com.bob.project.config.mvc.kafka.producer.PartitionSelecter;

/**
 * Kafka生产者/消费者工厂的配置提供类
 * 
 * @since 2017年7月9日 下午12:50:37
 * @version $Id$
 * @author JiangJibo
 *
 */
public abstract class KafkaContainerFactoryConfigurar {

	/**
	 * 创建KafkaProducerFactory,每一个工厂都只会生成一个KafkaProducer,
	 * 若想要发送多种类型的消息则需要定义多个KafkaProducerFactory和相应的KafkaTemplate
	 * 
	 * @return
	 */
	public static ProducerFactory<Integer, String> getKafkaProducerFactory() {
		Map<String, Object> configs = new HashMap<String, Object>();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaContextConstant.KAFKA_SERVERS_CONFIG);
		configs.put(ProducerConfig.RETRIES_CONFIG, 3);
		configs.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		configs.put(ProducerConfig.CLIENT_ID_CONFIG, "project");
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, PartitionSelecter.class);
		return new DefaultKafkaProducerFactory<Integer, String>(configs);
	}

	/**
	 * 配置ConsumerFactory,这些属性所有的Container共享,即一个ContainerFactory生成的所有Container共享ConsumerFactory
	 * 
	 * @see ConcurrentKafkaListenerContainerFactory#createContainerInstance(KafkaListenerEndpoint)
	 * 
	 * @return
	 */
	public static ConsumerFactory<Integer, String> getDefaultKafkaConsumerFactory() {
		Map<String, Object> configs = createDefaultConsumerFactoryConfigs();
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaContextConstant.DEFAULT_KAFKA_GROUP_ID);
		return new DefaultKafkaConsumerFactory<Integer, String>(configs);
	}

	public static ConsumerFactory<Integer, String> getLoggingKafkaConsumerFactory() {
		Map<String, Object> configs = createDefaultConsumerFactoryConfigs();
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaContextConstant.LOGGING_KAFKA_GROUP_ID);
		return new DefaultKafkaConsumerFactory<Integer, String>(configs);
	}

	/**
	 * 创建KafkaConsumerFactory的默认配置信息
	 * 
	 * @return
	 */
	private static Map<String, Object> createDefaultConsumerFactoryConfigs() {
		Map<String, Object> configs = new HashMap<String, Object>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaContextConstant.KAFKA_SERVERS_CONFIG);
		configs.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 3000L); // poll请求失败时到下次请求的间隔
		configs.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 3000L); // 当poll得到0条records时隔多久再次poll
		configs.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 300000L); // 设置Broker空闲多久会关闭
		configs.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000); // 批量poll的间隔,若消息已经消费完则会马上poll
		configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 30); // 从Kafka poll消息时,每次最多poll 30条
		// configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 不支持自动Commit Ack
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return configs;
	}

}
