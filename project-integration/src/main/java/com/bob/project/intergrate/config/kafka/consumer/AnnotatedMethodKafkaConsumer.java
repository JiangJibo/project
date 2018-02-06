/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.intergrate.config.kafka.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bob.project.intergrate.config.kafka.entity.KafkaMessageEntity;
import com.bob.project.intergrate.config.kafka.constant.KafkaContextConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * @since 2017年6月28日 下午7:48:06
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class AnnotatedMethodKafkaConsumer {

	final static Logger LOGGER = LoggerFactory.getLogger(AnnotatedMethodKafkaConsumer.class);

	private final Gson gson = new Gson();

	/**
	 * Exactly Once的一个预想方案:
	 * 使用BatchListener,一次Poll的数据一起解析,当Poll的数据都解析完之后将解析的最后一个Offset持久化,按批次更新Offset,不能精确定位消费过的Offset,
	 * 但对吞吐量影响较小
	 * 
	 * 使用RecordListener,每消费一条消息更新持久化的Offset,能精确定位消费过的Offset,但对吞吐量有一定影响
	 * 
	 * 当因为网络波动造成的重复Poll,通过判断当前Offset是否小于消费数据的首个Offset来去除重复消费的数据 ;当因为web服务器宕机,重启项目时读取Offset,同理去重
	 * 
	 * @param data
	 * @throws InterruptedException
	 */
	@KafkaListener(id = "annotatedMethodKafkaConsumer#0", topics = KafkaContextConstant.KAFKA_TOPIC)
	/*@KafkaListener(id = "annotatedMethodKafkaConsumer#0", topicPartitions = { @TopicPartition(topic = KafkaContextConstant.KAFKA_TOPIC, partitions = "0") })*/
	/*@KafkaListener(id = "annotatedMethodKafkaConsumer#0", topicPartitions = {
			@TopicPartition(topic = KafkaContextConstant.KAFKA_TOPIC, partitionOffsets = { @PartitionOffset(partition = "0", initialOffset = "0") }) })*/
	public void consume0(ConsumerRecord<Integer, String> data) throws InterruptedException {
		LOGGER.debug("当前对象的内存名称为:[{}]", this.toString());
		LOGGER.debug("当前线程名称[{}],载荷值[{}]", Thread.currentThread().getName(), data.value());
		LOGGER.debug("Topic：[{}]", data.topic());
		LOGGER.debug("Key：[{}]", data.key());
		LOGGER.debug("Offset：[{}]", data.offset());
		LOGGER.debug("Partition：[{}]", data.partition());
		LOGGER.debug("Checksum：[{}]", data.checksum());
		LOGGER.debug("TimestampType：[{}]", data.timestampType());
		LOGGER.debug("Timestamp：[{}]", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(data.timestamp())));
		Thread.sleep(1000);
		// ack.acknowledge(); // 手动确认消息Ack,看注释得出:这个操作意味着在这条消息之前的所有消息都已经被处理过了,也就是说Offset被移到了这条消息上
	}

	/*@KafkaListener(id = "annotatedMethodKafkaConsumer#1", topicPartitions = {
			@TopicPartition(topic = KafkaContextConstant.KAFKA_TOPIC, partitionOffsets = { @PartitionOffset(initialOffset = "0", partition = "0") }) })*/
	public void consume1(Message<KafkaMessageEntity> entity) {
		LOGGER.debug("当前线程名称[{}],载荷值[{}]", Thread.currentThread().getName(), gson.toJson(entity.getPayload()));
		MessageHeaders headers = entity.getHeaders();
		LOGGER.debug("MessageHeaders：[{}]", headers.toString());
		LOGGER.debug("Topic：[{}]", headers.get("kafka_receivedTopic", String.class));
		LOGGER.debug("Key：[{}]", headers.get("kafka_receivedMessageKey", Integer.class));
		LOGGER.debug("Offset：[{}]", headers.get("kafka_offset", Long.class));
		LOGGER.debug("Partition：[{}]", headers.get("kafka_receivedPartitionId", Integer.class));
	}

	/**
	 * 不通过@KafkaListener注解来配置Endpoint,而通过手动配置成BatchMessageListener
	 * MessageConverter接口不提供返回List<Message<?>>的方法,所以批量处理时只能是 List<ConsumerRecord<?,?>>形式
	 * 
	 * @see #BatchMessageConverter
	 * @param data
	 * @param ack
	 * @throws InterruptedException
	 */
	public void consume2(List<ConsumerRecord<Integer, String>> data) throws InterruptedException {
		LOGGER.debug("************************************");
		ConsumerRecord<Integer, String> record = null;
		for (int i = 0; i < data.size(); i++) {
			record = data.get(i);
			LOGGER.debug("当前对象的内存名称为:[{}]", this.toString());
			LOGGER.debug("当前线程名称[{}],载荷值[{}]", Thread.currentThread().getName(), record.value());
			LOGGER.debug("Topic：[{}]", record.topic());
			LOGGER.debug("Key：[{}]", record.key());
			LOGGER.debug("Offset：[{}]", record.offset());
			LOGGER.debug("Partition：[{}]", record.partition());
			LOGGER.debug("Checksum：[{}]", record.checksum());
			LOGGER.debug("TimestampType：[{}]", record.timestampType());
			LOGGER.debug("Timestamp：[{}]", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(record.timestamp())));
			Thread.sleep(500);
		}
		LOGGER.debug("************************************");
	}

}
