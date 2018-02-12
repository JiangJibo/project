package com.bob.test.concrete.rocket;

import com.bob.intergrate.rocket.RocketConsumer;
import com.bob.intergrate.rocket.RocketContextConfig;
import com.bob.test.config.TestContextConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Rocket消息消费者测试
 *
 * @author wb-jjb318191
 * @create 2018-02-11 15:23
 */
@ContextConfiguration(classes = RocketContextConfig.class)
public class RocketmqConsumerTest extends TestContextConfig {

    @Autowired
    private RocketConsumer rocketConsumer;

    @Test
    public void consumerRocketmq() throws InterruptedException {
        rocketConsumer.consume1();
        Thread.sleep(1000000);
    }

}
