package com.bob.intergrate.rocket;

import java.util.Properties;

import javax.annotation.PostConstruct;

import com.bob.intergrate.rocket.ann.EnableRocket;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Rocket消息队列配置
 *
 * @author wb-jjb318191
 * @create 2018-02-11 15:09
 */
@Configuration
@EnableRocket
@PropertySource(name = "rocket-config", value = "classpath:rocket-config.properties")
public class RocketContextConfig {

    @Autowired
    private ConfigurableEnvironment environment;

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketContextConfig.class);

    static {
        //设置Rocket的日志
        ClientLogger.setLog(LOGGER);
    }

    /**
     * 将Rocket-config.properties内的配置信息配置到环境中
     */
    @PostConstruct
    private void initRocketContext() {
        Properties properties = (Properties)environment.getPropertySources().get("rocket-config").getSource();
        for (String key : properties.stringPropertyNames()) {
            System.setProperty(key, properties.getProperty(key));
        }
    }

    @Bean
    public RocketConsumerConfiguration rocketMQConsumerConfiguration() {
        return new RocketConsumerConfiguration();
    }

    //@Bean
    public DefaultMQPushConsumer rocketMQPushConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("rmq_group");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setVipChannelEnabled(false);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        return consumer;
    }

    @Bean
    public DefaultMQProducer RocketMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("rmq_group");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setInstanceName("192.168.0.1@360");
        // 必须设为false否则连接broker10909端口
        producer.setVipChannelEnabled(false);
        producer.start();
        return producer;
    }

}
