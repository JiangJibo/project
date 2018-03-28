package com.bob.common.utils.rocket.processor;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PreDestroy;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.CollectionUtils;

/**
 * RocketMQ Consumer生命周期处理器
 *
 * @author wb-jjb318191
 * @create 2018-03-20 9:41
 */
public class RocketConsumerLifecycleManager implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketConsumerLifecycleManager.class);

    @Autowired(required = false)
    private Map<String, DefaultMQPushConsumer> rocketMQConsumers;

    private volatile boolean isRunning = false;

    @Override
    public void start() {
        if (CollectionUtils.isEmpty(rocketMQConsumers)) {
            return;
        }
        for (Entry<String, DefaultMQPushConsumer> entry : rocketMQConsumers.entrySet()) {
            try {
                entry.getValue().start();
            } catch (MQClientException e) {
                LOGGER.error("启动[{}]消费者失败", entry.getKey(), e);
            }
        }
        isRunning = true;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
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
        if (CollectionUtils.isEmpty(rocketMQConsumers)) {
            return;
        }
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
