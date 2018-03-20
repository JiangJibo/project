package com.bob.test.concrete.rocket;

import java.util.Set;

import com.bob.intergrate.rocket.RocketContextConfig;
import com.bob.test.config.TestContextConfig;
import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketmqConsumerTest.class);

    @Autowired
    private DefaultMQPushConsumer rocketConsumer;
    private static final String TOPIC = "test_topic";
    private static final String TAG = "*";

    @Before
    public void init() throws MQClientException {
        rocketConsumer.subscribe(TOPIC, TAG);
        rocketConsumer.registerMessageListener((MessageListenerConcurrently)(list, context) -> {
            MessageExt msg = list.get(0);
            LOGGER.debug("从QueueId:[{}]处发回消息[{}],重试次数:[{}]", msg.getQueueId(), new String(msg.getBody()), msg.getReconsumeTimes());
            msg.setReconsumeTimes(0);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        });
        rocketConsumer.start();
    }

    @After
    public void destory() {
        rocketConsumer.shutdown();
    }

    @Test
    public void startConsuming() throws InterruptedException {
        Thread.sleep(1000 * 60 * 3);
    }

    @Test
    public void sendMessageBack() throws InterruptedException {

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
        Set<MessageQueue> messageQueues = rocketConsumer.fetchSubscribeMessageQueues(TOPIC);
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
     * 获取指定MsgId的消息
     *
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    @Test
    public void viewMessage() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        MessageExt messageExt = rocketConsumer.viewMessage("C0A80B6600002A9F00000000000087C7");
        System.out.println(String.format("消息内容:[%s]", new String(messageExt.getBody())));
        System.out.println(gson.toJson(messageExt));
    }

    private void sendMessageBack(MessageExt messageExt, int delayLevel) throws Exception {
        rocketConsumer.sendMessageBack(messageExt, delayLevel);
    }

}
