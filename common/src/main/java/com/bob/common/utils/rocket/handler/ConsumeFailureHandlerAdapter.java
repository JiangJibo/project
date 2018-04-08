package com.bob.common.utils.rocket.handler;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageClientExt;

/**
 * 消费失败处理器适配器
 *
 * @author Administrator
 * @create 2018-04-08 18:59
 */
public class ConsumeFailureHandlerAdapter implements ConsumeFailureHandler {

    @Override
    public ConsumeOrderlyStatus handlerOrderlyFailure(MessageClientExt msg, ConsumeOrderlyContext context, int maxReconsumeTimes) {
        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
    }

    @Override
    public ConsumeOrderlyStatus handlerOrderlyFailure(MessageClientExt msg, ConsumeOrderlyContext context, int maxReconsumeTimes, Exception ex) {
        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
    }

    @Override
    public ConsumeConcurrentlyStatus handlerConcurrentlyFailure(MessageClientExt msg, ConsumeConcurrentlyContext context, int maxReconsumeTimes) {
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    @Override
    public ConsumeConcurrentlyStatus handlerConcurrentlyFailure(MessageClientExt msg, ConsumeConcurrentlyContext context, int maxReconsumeTimes, Exception ex) {
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }
}
