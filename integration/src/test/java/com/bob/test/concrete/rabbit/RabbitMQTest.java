package com.bob.test.concrete.rabbit;

import com.bob.intergrate.rabbit.RabbitContextConfig;
import com.bob.intergrate.rabbit.producer.RabbitProducer;
import com.bob.test.config.TestContextConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author wb-jjb318191
 * @create 2018-02-08 10:07
 */
@ContextConfiguration(classes = RabbitContextConfig.class)
public class RabbitMQTest extends TestContextConfig {

    private Logger logger = LoggerFactory.getLogger(RabbitMQTest.class);

    @Test
    public void should_send_a_amq_message() throws Exception {
        RabbitProducer messageProducer = webApplicationContext.getBean("rabbitProducer", RabbitProducer.class);
        int a = 100;
        while (a > 0) {
            messageProducer.sendMessage("Hello, I am amq sender num :" + a--);
            try {
                //暂停一下，好让消息消费者去取消息打印出来
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
