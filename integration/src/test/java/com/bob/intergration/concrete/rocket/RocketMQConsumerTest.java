package com.bob.intergration.concrete.rocket;

import java.util.Set;

import com.bob.intergrate.rocket.RocketContextConfig;
import com.bob.intergration.config.TestContextConfig;
import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageClientExt;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant.TOPIC;

/**
 * Rocket消息消费者测试
 *
 * @author wb-jjb318191
 * @create 2018-02-11 15:23
 */
@ContextConfiguration(classes = RocketContextConfig.class)
public class RocketMQConsumerTest extends TestContextConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerTest.class);

    @Autowired
    private DefaultMQPushConsumer rocketConsumer;

    @After
    public void destory() {
        rocketConsumer.shutdown();
    }

    @Test
    public void startConsuming() throws InterruptedException {
        Thread.sleep(1000 * 60 * 10);
    }

    /**
     * 根据Key查询消息
     *
     * @throws MQClientException
     * @throws InterruptedException
     */
    @Test
    public void queryMessageByKey() throws MQClientException, InterruptedException {
        QueryResult result = rocketConsumer.queryMessage(TOPIC, "index:35", 1, 0, System.currentTimeMillis());
        MessageExt messageExt = result.getMessageList().get(0);
        System.out.println(String.format("消息内容是:%s", new String(messageExt.getBody())));
    }

    /**
     * 获取指定Topic的所有MessageQueue
     *
     * @throws MQClientException
     */
    @Test
    public void fetchSubscribeMessageQueues() throws MQClientException {
        Set<MessageQueue> messageQueues = rocketConsumer.fetchSubscribeMessageQueues("service-topic");
        for (MessageQueue mq : messageQueues) {
            System.out.println(gson.toJson(mq));
        }
    }

    /**
     * 查询指定MessageQueue的MaxOffset
     *
     * @throws MQClientException
     */
    @Test
    public void queryMessageQueueMaxOffset() throws MQClientException {
        MessageQueue messageQueue = rocketConsumer.fetchSubscribeMessageQueues(TOPIC).iterator().next();
        long consumeQueueOffset = rocketConsumer.maxOffset(messageQueue);
        System.out.println(String.format("Topic:%s,Broker:%s,QueueId:%d,MaxOffset:%d", messageQueue.getTopic(), messageQueue.getBrokerName(),
            messageQueue.getQueueId(), consumeQueueOffset));
    }

    /**
     * 获取指定offsetMsgId的消息
     *
     * @throws Exception
     */
    @Test
    public void viewMessageByOffsetMsgId() throws Exception {
        MessageClientExt messageExt = (MessageClientExt)rocketConsumer.viewMessage("1E05407100002A9F000000000000BB50");
        System.out.println(String.format("消息内容:[%s]", new String(messageExt.getBody())));
        System.out.println(gson.toJson(messageExt));
    }

    /**
     * 通过uniqueKey获取指定topic的Message
     *
     * @throws Exception
     */
    @Test
    public void viewMessageByUniqueKey() throws Exception {
        MessageClientExt messageExt = (MessageClientExt)rocketConsumer.viewMessage("tx", "1E05402D210C14DAD5DC0C6DA7AF0000");
        System.out.println(String.format("消息内容:[%s]", new String(messageExt.getBody())));
        System.out.println(gson.toJson(messageExt));
    }



    @Test
    public void sendMessageBack() throws InterruptedException {
    }

}
