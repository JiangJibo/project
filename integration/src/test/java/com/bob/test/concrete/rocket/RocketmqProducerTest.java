package com.bob.test.concrete.rocket;

import com.bob.intergrate.rocket.RocketContextConfig;
import com.bob.intergrate.rocket.RocketProducer;
import com.bob.test.config.TestContextConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author wb-jjb318191
 * @create 2018-02-11 15:09
 */

@ContextConfiguration(classes = RocketContextConfig.class)
public class RocketmqProducerTest extends TestContextConfig {

    @Autowired
    private RocketProducer rocketProducer;

    @Test
    public void testCreateTopic() throws MQClientException {
        rocketProducer.createTopic();

    }

    @Test
    public void testProduceMsg() throws MQClientException {
        rocketProducer.produce1();
    }

}
