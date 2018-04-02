package com.bob.intergration.concrete.rocket;

import java.util.List;
import java.util.Random;

import com.bob.intergrate.rocket.RocketContextConfig;
import com.bob.intergration.config.TestContextConfig;
import com.bob.root.utils.model.RootUser;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.bob.intergrate.rocket.producer.selector.DefaultMessageQueueSelector.Selector.FIRST;

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
        for (int i = 0; i < 100; i++) {
            Message message = new Message("orderly-topic", new String("测试信息:[" + i + "]").getBytes());
            rocketMQProducer.send(message, messageQueueSelector, FIRST);
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
                System.out.println(gson.toJson(rocketMQProducer.send(msg)));
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
    public void sendMessageInTransaction() throws MQClientException {
        RootUser user = new RootUser();
        user.setId(1002);
        user.setName("lanboal");
        user.setAdress("杭州");
        user.setAge(30);
        user.setPassword("123456");
        user.setTelephone("18758107760");
        Message message = new Message("tx", "user", gson.toJson(user).getBytes());
        TransactionSendResult sendResult = transactionMQProducer.sendMessageInTransaction(message, transactionExecuter, RootUser.class);
        System.out.println(sendResult.getLocalTransactionState().toString());
    }

    private List<MessageQueue> fetchPublishMessageQueues() throws MQClientException {
        return rocketMQProducer.fetchPublishMessageQueues("test-topic");
    }

}
