package com.bob.intergrate.rocket;

import com.bob.intergrate.rocket.ann.RocketListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * RocketMQ消费者配置
 *
 * @author wb-jjb318191
 * @create 2018-03-20 16:06
 */
public class RocketMQConsumerConfiguration {

    @RocketListener(consumerGroup = "project", topic = "test-topic")
    public boolean consumeDefault(MessageExt msg, ConsumeConcurrentlyContext context) {
        return true;
    }

}
