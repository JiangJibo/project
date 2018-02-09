package com.bob.intergrate.rabbit.producer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author wb-jjb318191
 * @create 2018-02-08 9:58
 */
public class RabbitProducer {

    private Logger logger = LoggerFactory.getLogger(RabbitProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbit.queue.default.key}")
    private String defaultKey;

    @Value("${rabbit.queue.service.key}")
    private String serviceKey;

    public void sendMessage(Object message) throws IOException {
        logger.info("发送Message:{}", message);
        rabbitTemplate.convertAndSend(defaultKey, message);
        rabbitTemplate.convertAndSend(serviceKey, message);
    }
}
