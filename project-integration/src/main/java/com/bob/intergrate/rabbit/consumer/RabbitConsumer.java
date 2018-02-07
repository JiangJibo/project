package com.bob.intergrate.rabbit.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Rabbit监听器
 *
 * @author wb-jjb318191
 * @create 2018-02-07 15:25
 */
@Component
public class RabbitConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConsumer.class);

    @RabbitListener
    public void listener(String message) {
        LOGGER.info("消费消息{}", message);
    }

}
