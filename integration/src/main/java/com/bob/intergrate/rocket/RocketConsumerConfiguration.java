package com.bob.intergrate.rocket;

import com.bob.intergrate.rocket.ann.RocketListener;
import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RocketMQ消费者配置
 *
 * @author wb-jjb318191
 * @create 2018-03-20 16:06
 */
public class RocketConsumerConfiguration {

    private Gson gson = new Gson();

    @Autowired
    private DefaultMQPushConsumer test_topic_rmq_group;

    @RocketListener(consumerGroup = "rmq_group", topic = "test_topic", namesrvAddr = "127.0.0.1:9876")
    public boolean consumeDefault(MessageExt msg, ConsumeConcurrentlyContext context) {
        System.out.println(gson.toJson(msg));
        return true;
    }

}
