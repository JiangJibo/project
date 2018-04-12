package com.bob.intergrate.rocket.consumer;

import java.util.HashSet;
import java.util.Set;

import com.bob.common.utils.rocket.ann.RocketListener;
import com.bob.common.utils.rocket.constant.RocketBeanDefinitionConstant;
import com.bob.intergrate.rocket.consumer.failurehandler.DefaultConsumeFailureHandler;
import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.factory.MQClientInstance;
import org.apache.rocketmq.common.message.MessageClientExt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.rocketmq.common.message.MessageConst.PROPERTY_DELAY_TIME_LEVEL;

/**
 * RocketMQ消费者配置
 *
 * @author wb-jjb318191
 * @create 2018-03-20 16:06
 */
@Configuration
public class RocketConsumerConfiguration {

    private Gson gson = new Gson();

    /**
     * 通过{@link RocketListener}形式定义的Consumer,名称形式以为：
     * consumeMethodName + {@link RocketBeanDefinitionConstant#ROCKETMQ_CONSUMER_BEAN_NAME_SUFFIX}
     * BeanName形式可自定义
     */
    //@Autowired
    private DefaultMQPushConsumer orderlyRocketConsumer;

    @Bean
    public DefaultConsumeFailureHandler defaultConsumeFailureHandler(){
        return new DefaultConsumeFailureHandler();
    }

    /**
     * 定义RocketMQ消费器
     *
     * @param msg
     * @param context
     * @return true:消费成功;  false:消费失败,发回给Broker,一段时间后重试
     */
    //@RocketListener(configProperties = "rocket-concurrently-config.properties", faliureHandler = "defaultConsumeFailureHandler")
    public boolean concurrently(MessageClientExt msg, ConsumeConcurrentlyContext context) {
        String delay = msg.getProperty(PROPERTY_DELAY_TIME_LEVEL);
        int reconsumeTimes = msg.getReconsumeTimes();
        System.out.println(String.format("延迟级别: [%s], 重消费次数:[%d], 消费内容为[%s]", delay, reconsumeTimes, new String(msg.getBody())));
        throw new IllegalArgumentException("测试消费时抛出异常");
    }

    /**
     * 订阅死信队列
     * SendMessageProcessor 在创建死信队列时,指定其 perm = write
     * {@link MQClientInstance#topicRouteData2TopicSubscribeInfo}时,因为其不可读,所以不会生成MessageQueue
     * 也就是会生成ConsumeQueue,但是不能直接订阅
     *
     * @param msg
     * @return
     */
    //@RocketListener(consumerGroup = "dlqConsumerGroup", topic = "%DLQ%concurrently-consume", namesrvAddr = "127.0.0.1:9876")
    public boolean concurrentlyDLQ(MessageClientExt msg) {
        System.out.println(String.format("死信队列消息内容为 [%s]", new String(msg.getBody())));
        return true;
    }

    /**
     * 创建有序消费者
     *
     * @param msg
     * @param context
     * @return
     */
    //@RocketListener(orderly = true, configProperties = "rocket-orderly-config.properties")
    public boolean orderly(MessageClientExt msg, ConsumeOrderlyContext context) {
        String threadName = Thread.currentThread().getName();
        System.out.println(String.format("threadName:[%s], QueueOffset:[%d]", threadName, msg.getQueueOffset()));
        return true;
    }

    /**
     * 订阅事务消息
     *
     * @param msg
     * @param context
     * @return
     */
    //@RocketListener(consumerGroup = "myGroup", topic = "tx", tag = "user", namesrvAddr = "127.0.0.1:9876")
    public boolean tx(MessageClientExt msg, ConsumeConcurrentlyContext context) {
        System.out.println(gson.toJson(msg));
        String msgId = msg.getMsgId();
        System.out.println("#################[" + msgId + "]#################");
        System.out.println("#################[" + new String(msg.getBody()) + "]#################");
        return true;
    }

    //@Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPullConsumer defaultMQPullConsumer() throws MQClientException {
        DefaultMQPullConsumer pullConsumer = new DefaultMQPullConsumer();
        pullConsumer.setNamesrvAddr("127.0.0.1:9876");
        pullConsumer.setConsumerGroup("pull-group");
        Set<String> topics = new HashSet<>();
        topics.add("%DLQ%my-group");
        pullConsumer.setRegisterTopics(topics);
        return pullConsumer;
    }
}
