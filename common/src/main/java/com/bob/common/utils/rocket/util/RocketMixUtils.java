package com.bob.common.utils.rocket.util;

import com.bob.common.utils.rocket.listener.AbstractMessageListener;
import com.bob.common.utils.rocket.processor.RocketListenerAnnotationBeanPostProcessor;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;

/**
 * 工具类
 *
 * @author wb-jjb318191
 * @create 2018-04-08 15:21
 */
public class RocketMixUtils {

    /**
     * 获取指定消费者的最大消费次数
     *
     * @param messageListener
     * @return
     */
    public static int getMaxReconsumeTimes(AbstractMessageListener messageListener) {
        DefaultMQPushConsumer consumer = RocketListenerAnnotationBeanPostProcessor.getConsumerByMessageListener(messageListener);
        return consumer.getMaxReconsumeTimes();
    }

}
