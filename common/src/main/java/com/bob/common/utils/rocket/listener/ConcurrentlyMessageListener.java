package com.bob.common.utils.rocket.listener;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;

import com.bob.common.utils.rocket.ann.RocketListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * 封装{@link RocketListener}标识的方法成一个消息并发消费器
 *
 * @author wb-jjb318191
 * @create 2018-03-20 10:03
 */
public class ConcurrentlyMessageListener extends RocketMessageListener implements MessageListenerConcurrently {

    public ConcurrentlyMessageListener(Object consumeBean, Method consumeMethod) {
        super(consumeBean, consumeMethod);
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        Object[] args = buildConsumeArguments(msgs, context);
        boolean result = (boolean)ReflectionUtils.invokeMethod(consumeMethod, consumeBean, args);
        return result ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

}
