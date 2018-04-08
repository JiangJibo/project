package com.bob.common.utils.rocket.listener;

import java.lang.reflect.Method;
import java.util.List;

import com.bob.common.utils.rocket.ann.RocketListener;
import com.bob.common.utils.rocket.handler.ConsumeFailureHandler;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageClientExt;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

/**
 * 封装{@link RocketListener}方法,生成有序的消息监听器
 *
 * @author wb-jjb318191
 * @create 2018-04-02 16:24
 */
public class OrderlyMessageListener extends AbstractMessageListener implements MessageListenerOrderly {

    public OrderlyMessageListener(Object consumeBean, Method consumeMethod, ConsumeFailureHandler failureHandler) {
        super(consumeBean, consumeMethod, failureHandler);
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        Object[] args = buildConsumeArguments(msgs, context);
        boolean result;
        Exception ex = null;
        try {
            result = (boolean)ReflectionUtils.invokeMethod(consumeMethod, consumeBean, args);
            if (result) {
                return ConsumeOrderlyStatus.SUCCESS;
            }
        } catch (Exception e) {
            ex = e;
        }
        MessageClientExt msg = (MessageClientExt)msgs.get(0);
        warnWhenConsumeFailed(msg, ex);
        if (ex == null) {
            return failureHandler.handlerOrderlyFailure(msg, context, getMaxReconsumeTimes());
        } else {
            return failureHandler.handlerOrderlyFailure(msg, context, getMaxReconsumeTimes(), ex);
        }
    }
}
