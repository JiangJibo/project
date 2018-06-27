package com.bob.integrate.rocket.consumer.failurehandler;

import com.bob.common.utils.rocket.handler.ConsumeFailureHandlerAdapter;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageClientExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消费失败处理器
 *
 * @author Administrator
 * @create 2018-04-08 19:40
 */
public class DefaultConsumeFailureHandler extends ConsumeFailureHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConsumeFailureHandler.class);

    @Override
    public ConsumeConcurrentlyStatus handlerConcurrentlyFailure(MessageClientExt msg, ConsumeConcurrentlyContext context, int maxReconsumeTimes) {
        if (msg.getReconsumeTimes() == maxReconsumeTimes) {
            LOGGER.info("当前消息即将被纳入死信队列");
        }
        LOGGER.info("自定义消费失败处理器");
        return super.handlerConcurrentlyFailure(msg, context, maxReconsumeTimes);
    }

    @Override
    public ConsumeConcurrentlyStatus handlerConcurrentlyFailure(MessageClientExt msg, ConsumeConcurrentlyContext context, int maxReconsumeTimes, Exception ex) {
        LOGGER.info("自定义消费失败处理器");
        return super.handlerConcurrentlyFailure(msg, context, maxReconsumeTimes, ex);
    }
}
