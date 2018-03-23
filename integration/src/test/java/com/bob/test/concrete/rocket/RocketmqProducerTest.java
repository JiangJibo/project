package com.bob.test.concrete.rocket;

import java.util.List;
import java.util.Random;

import com.bob.intergrate.rocket.RocketContextConfig;
import com.bob.test.config.TestContextConfig;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.bob.intergrate.rocket.producer.selector.DefaultMessageQueueSelector.Selector.SECOND;

/**
 * 生产者测试类
 *
 * @author wb-jjb318191
 * @create 2018-02-11 15:09
 */
@ContextConfiguration(classes = RocketContextConfig.class)
public class RocketmqProducerTest extends TestContextConfig {

    @Autowired
    private DefaultMQProducer rocketProducer;

    @Autowired
    private MessageQueueSelector messageQueueSelector;

    private static final Message MESSAGE = new Message("test-topic", new String("测试信息").getBytes());

    /**
     * 自主选择发送到哪个MessageQueue,有序消息的前提
     *
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    @Test
    public void testSendWithSelector() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        rocketProducer.send(MESSAGE, messageQueueSelector, SECOND);
    }

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

    @Test
    private List<MessageQueue> fetchPublishMessageQueues() throws MQClientException {
        return rocketProducer.fetchPublishMessageQueues("test-topic");
    }

}
