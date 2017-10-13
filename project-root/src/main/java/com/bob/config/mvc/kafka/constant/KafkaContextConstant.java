/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.kafka.constant;

/**
 * Kafka生产者/消费者工厂的常量配置信息
 * 
 * @since 2017年7月9日 下午12:57:18
 * @version $Id$
 * @author JiangJibo
 *
 */
public interface KafkaContextConstant {

	/**
	 * Kafka服务器地址
	 */
	public static final String KAFKA_SERVERS_CONFIG = "192.168.5.223:9092,192.168.5.223:9093";

	public static final String KAFKA_TOPIC = "lanboal";

	public static final String DEFAULT_KAFKA_GROUP_ID = "lanboal";

	public static final String LOGGING_KAFKA_GROUP_ID = "logging";

}
