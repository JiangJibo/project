package com.bob.intergrate.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Rabbit监听器
 *
 * @author wb-jjb318191
 * @create 2018-02-07 15:25
 */
@Component
//@RabbitListener
public class RabbitConsumer {

    @RabbitListener(queues = "")
    public void listener() {

    }

}
