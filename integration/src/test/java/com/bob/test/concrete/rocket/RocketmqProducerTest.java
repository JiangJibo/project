package com.bob.test.concrete.rocket;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.bob.intergrate.rocket.RocketContextConfig;
import com.bob.test.config.TestContextConfig;
import com.google.gson.Gson;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
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
    private DefaultMQProducer rocketProducer;

    @Test
    public void testProduceMsg() {
        try {
            //List<MessageQueue> messageQueues = fetchPublishMessageQueues();
            for (int i = 0; i < 4; i++) {
                String tag = new Random().nextInt() % 2 == 1 ? "odd" : "even";
                Message msg = new Message("test-topic", tag,
                    String.format("第[%d]:条Retry信息", i).getBytes()
                );
                msg.setKeys("index:" + i);
                System.out.println(gson.toJson(rocketProducer.send(msg)));
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        rocketProducer.shutdown();
    }

    private List<MessageQueue> fetchPublishMessageQueues() throws MQClientException {
        return rocketProducer.fetchPublishMessageQueues("test-topic");
    }

}
