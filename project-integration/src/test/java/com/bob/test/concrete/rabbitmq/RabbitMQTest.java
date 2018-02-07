package com.bob.test.concrete.rabbitmq;

import com.bob.test.config.BaseControllerTest;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RabbitMQ测试用例
 *
 * @author wb-jjb318191
 * @create 2018-02-07 14:42
 */
public class RabbitMQTest extends BaseControllerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessage() {
        String msg = "123456";
        rabbitTemplate.send(new Message(msg.getBytes(), new MessageProperties()));
    }

}
