/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.kafka.producer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 获取分区计算基数
 * 
 * @since 2017年8月12日 下午2:47:29
 * @version $Id$
 * @author JiangJibo
 *
 */
public abstract class PartitionerRadixProcessor {

	final static Logger LOGGER = LoggerFactory.getLogger(PartitionerRadixProcessor.class);

	private static final String RADIX_CONFIGURATION_PATH = "F:/kafka_2.12/kafka-partition-radix.properties";
	private static final String RADIX_CONFIGURAR_KEY = "partitioner_compute_radix";

	private PartitionerRadixProcessor() {

	}

	/**
	 * 从指定的配置文件获取分区计算基数
	 * 
	 * @return
	 * @throws IOException
	 */
	public static int retrieveRadixFromProperties() {
		int radix = -1;
		try {
			String radixStr = PropertiesLoaderUtils.loadProperties(new FileSystemResource(RADIX_CONFIGURATION_PATH)).get(RADIX_CONFIGURAR_KEY).toString();
			radix = Integer.valueOf(radixStr);
		} catch (IOException e) {
			LOGGER.error("加载Kafka分区基数配置文件时出现异常,文件路径为:[{}]", RADIX_CONFIGURATION_PATH);
		}
		return radix;
	}

}
