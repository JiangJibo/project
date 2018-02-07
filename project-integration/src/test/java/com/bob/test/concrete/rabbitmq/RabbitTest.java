package com.bob.test.concrete.rabbitmq;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.bob.intergrate.rabbit.producer.Sender;
import com.bob.test.config.BaseControllerTest;
import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RabbitMQ测试用例
 *
 * @author wb-jjb318191
 * @create 2018-02-07 14:42
 */
public class RabbitTest extends BaseControllerTest {

    private final static String QUEUE_NAME = "MyQueue";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessage() throws InterruptedException, IOException, TimeoutException {
        String message = "hello " + new Date();
        System.out.println("Sender : " + message);
        //rabbitTemplate.convertAndSend(context);
        //new Sender().send();
        Connection connection = rabbitTemplate.getConnectionFactory().createConnection();
        Channel channel = connection.createChannel(false);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        //关闭资源
        channel.close();
        connection.close();
    }

    @Test
    public void sendByTemplate() {
        String message = "hello " + new Date();
        System.out.println("Sender : " + message);
        rabbitTemplate.convertAndSend("lanboal","123456",message);
    }

}
