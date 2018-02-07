package com.bob.test.concrete.rabbitmq;

import java.util.Date;

import com.bob.test.config.BaseControllerTest;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RabbitMQ测试用例
 *
 * @author wb-jjb318191
 * @create 2018-02-07 14:42
 */
public class RabbitTest extends BaseControllerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessage() throws InterruptedException {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }

}
