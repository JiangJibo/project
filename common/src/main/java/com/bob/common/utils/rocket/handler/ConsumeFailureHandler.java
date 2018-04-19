package com.bob.common.utils.rocket.handler;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageClientExt;

/**
 * 消费失败处理器
 *
 * @author Administrator
 * @create 2018-04-08 18:53
 */
public interface ConsumeFailureHandler {

    /**
     * 处理有序消费失败情况
     *
     * @param msg
     * @param context
     * @param maxReconsumeTimes 消费最大消费次数
     * @return 处理后的结果
     */
    ConsumeOrderlyStatus handlerOrderlyFailure(MessageClientExt msg, ConsumeOrderlyContext context, int maxReconsumeTimes);

    /**
     * 处理有序消费失败情况
     *
     * @param msg
     * @param context
     * @param maxReconsumeTimes 消费最大消费次数
     * @param ex
     * @return 处理后的结果
     */
    ConsumeOrderlyStatus handlerOrderlyFailure(MessageClientExt msg, ConsumeOrderlyContext context, int maxReconsumeTimes, Exception ex);

    /**
     * 处理并发消费失败情况
     *
     * @param msg
     * @param context
     * @param maxReconsumeTimes 消费最大消费次数
     * @return 处理后的结果
     */
    ConsumeConcurrentlyStatus handlerConcurrentlyFailure(MessageClientExt msg, ConsumeConcurrentlyContext context, int maxReconsumeTimes);

    /**
     * 处理并发消费失败情况
     *
     * @param msg
     * @param context
     * @param maxReconsumeTimes 消费最大消费次数
     * @param ex
     * @return 处理后的结果
     */
    ConsumeConcurrentlyStatus handlerConcurrentlyFailure(MessageClientExt msg, ConsumeConcurrentlyContext context, int maxReconsumeTimes, Exception ex);

}