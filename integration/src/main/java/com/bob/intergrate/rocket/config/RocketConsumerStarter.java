package com.bob.intergrate.rocket.config;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PreDestroy;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;

/**
 * @author wb-jjb318191
 * @create 2018-03-20 9:41
 */
public class RocketConsumerStarter implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketConsumerStarter.class);

    @Autowired
    private Map<String, DefaultMQPushConsumer> rocketMQConsumers;

    private volatile boolean isRunning = false;

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void start() {
        for (Entry<String, DefaultMQPushConsumer> entry : rocketMQConsumers.entrySet()) {
            try {
                entry.getValue().start();
            } catch (MQClientException e) {
                LOGGER.error("启动[{}]消费者失败", entry.getKey());
            }
        }
        isRunning = true;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @PreDestroy
    public void destroy() {
        isRunning = false;
        for (DefaultMQPushConsumer consumer : rocketMQConsumers.values()) {
            consumer.shutdown();
        }
    }

    @Override
    public void stop(Runnable runnable) {

    }

    @Override
    public void stop() {

    }

}
