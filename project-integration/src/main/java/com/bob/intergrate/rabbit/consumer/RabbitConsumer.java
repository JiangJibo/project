package com.bob.intergrate.rabbit.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Rabbit监听器
 *
 * @author wb-jjb318191
 * @create 2018-02-07 15:25
 */
public class RabbitConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConsumer.class);

    @RabbitListener(queues = "${rabbit.queue.default}")
    public void defaultConsumer(Message message) {
        LOGGER.info("DefaultConsumer消费消息:{}", message);
    }

    @RabbitListener(queues = "${rabbit.queue.service}")
    public void serviceConsumer(Message message) {
        LOGGER.info("ServiceConsumer消费消息:{}", message);
    }

}
