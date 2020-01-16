package com.bob.intergration.concrete.rocket;

import java.util.List;
import java.util.Random;

import com.bob.integrate.rocket.RocketContextConfig;
import com.bob.intergration.config.TestContextConfig;
import com.bob.root.config.entity.RootUser;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.bob.integrate.rocket.producer.selector.DefaultMessageQueueSelector.Selector.FIRST;
import static com.bob.integrate.rocket.producer.selector.DefaultMessageQueueSelector.Selector.LAST;
import static com.bob.integrate.rocket.producer.selector.DefaultMessageQueueSelector.Selector.SECOND;
import static com.bob.integrate.rocket.producer.selector.DefaultMessageQueueSelector.Selector.THIRD;

/**
 * 生产者测试类
 *
 * @author wb-jjb318191
 * @create 2018-02-11 15:09
 */
@ContextConfiguration(classes = RocketContextConfig.class)
public class RocketMQProducerTest extends TestContextConfig {

    @Autowired
    private DefaultMQProducer rocketMQProducer;

    @Autowired
    private TransactionMQProducer transactionMQProducer;

    @Autowired
    private MessageQueueSelector messageQueueSelector;

    @Autowired
    private LocalTransactionExecuter transactionExecuter;

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
        for (int i = 0; i < 100; i++) {
            Message message = new Message("create-topic", new String("测试信息:[" + i + "]").getBytes());
            rocketMQProducer.send(message, messageQueueSelector, LAST);
        }
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
                SendResult sendResult = rocketMQProducer.send(msg);
                System.out.println(gson.toJson(sendResult));
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        rocketMQProducer.shutdown();
    }

    /**
     * 在事务中发送消息
     */
    @Test
    public void sendMessageInTransaction() throws Exception {
        RootUser user = new RootUser();
        user.setId(1002);
        user.setName("lanboal");
        user.setAdress("杭州");
        user.setAge(30);
        user.setPassword("123456");
        user.setPassword("123456");
        user.setTelephone("18758107760");
        Message message = new Message("tx", "user", gson.toJson(user).getBytes());
        TransactionSendResult sendResult = transactionMQProducer.sendMessageInTransaction(message, transactionExecuter, RootUser.class);
        System.out.println("msgId:[" + sendResult.getMsgId() + "]");
    }

    @Test
    public void fetchPublishMessageQueues() throws MQClientException {
        List<MessageQueue> queues = rocketMQProducer.fetchPublishMessageQueues("create-topic");
        System.out.println(queues.toString());
    }

    /**
     * 手动创建topic
     * 使用{@link MixAll#DEFAULT_TOPIC}作为Key来创建topic
     * Key的作用就是获取这个含有这个topic的所有Broker的addr
     * 每个Broker在向Namesrv注册时都会注册DEFAULT_TOPIC的Topic信息
     * 所以使用DEFAULT_TOPIC能够向所有的Broker注册Topic信息
     *
     * @throws MQClientException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQBrokerException
     */
    @Test
    public void createTopic() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        rocketMQProducer.createTopic(MixAll.DEFAULT_TOPIC, "create-topic", 4);
        Message message = new Message("create-topic", "Create Topic First Message".getBytes());
        SendResult sendResult = rocketMQProducer.send(message);
        System.out.println(sendResult.getMessageQueue().getQueueId());
    }

    /**
     * 测试死信队列
     */
    @Test
    public void testSendMessage() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message("my-topic", "测试死信队列消息".getBytes());
        rocketMQProducer.send(message);
    }

}
