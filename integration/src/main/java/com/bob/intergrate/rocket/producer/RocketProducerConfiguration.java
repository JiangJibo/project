package com.bob.intergrate.rocket.producer;

import com.bob.intergrate.rocket.producer.selector.DefaultMessageQueueSelector;
import com.bob.intergrate.rocket.producer.tx.DefaultTransactionCheckListener;
import com.bob.intergrate.rocket.producer.tx.DefaultTransactionExecuter;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ消费者配置类
 *
 * @author wb-jjb318191
 * @create 2018-03-23 16:06
 */
@Configuration
public class RocketProducerConfiguration {

    @Bean
    public DefaultMQProducer rocketMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("rmq_group");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setInstanceName("192.168.0.1@360");
        // 必须设为false否则连接broker10909端口
        producer.setVipChannelEnabled(false);
        producer.start();
        return producer;
    }

    @Bean
    public TransactionMQProducer transactionMQProducer() throws MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer("tx_group");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setInstanceName("192.168.0.1@110");
        // 必须设为false否则连接broker10909端口
        producer.setVipChannelEnabled(false);
        producer.setTransactionCheckListener(new DefaultTransactionCheckListener());
        producer.start();
        return producer;
    }

    @Bean
    public MessageQueueSelector messageQueueSelector() {
        return new DefaultMessageQueueSelector();
    }

    @Bean
    public LocalTransactionExecuter localTransactionExecuter() {
        return new DefaultTransactionExecuter();
    }

}
