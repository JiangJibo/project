package com.bob.intergrate.rocket;

import com.bob.intergrate.rocket.ann.RocketListener;
import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * RocketMQ消费者配置
 *
 * @author wb-jjb318191
 * @create 2018-03-20 16:06
 */
public class RocketConsumerConfiguration {

    private Gson gson = new Gson();

    @RocketListener(consumerGroup = "rmq-group", topic = "test-topic",namesrvAddr = "127.0.0.1:9876")
    public boolean consumeDefault(MessageExt msg, ConsumeConcurrentlyContext context) {
        System.out.println(gson.toJson(msg));
        return true;
    }

}
