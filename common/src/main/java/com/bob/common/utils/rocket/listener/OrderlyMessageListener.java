package com.bob.common.utils.rocket.listener;

import java.lang.reflect.Method;
import java.util.List;

import com.bob.common.utils.rocket.ann.RocketListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.ReflectionUtils;

/**
 * 封装{@link RocketListener}方法,生成有序的消息监听器
 *
 * @author wb-jjb318191
 * @create 2018-04-02 16:24
 */
public class OrderlyMessageListener extends AbstractMessageListener implements MessageListenerOrderly {

    public OrderlyMessageListener(Object consumeBean, Method consumeMethod) {
        super(consumeBean, consumeMethod);
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        Object[] args = buildConsumeArguments(msgs, context);
        boolean result = (boolean)ReflectionUtils.invokeMethod(consumeMethod, consumeBean, args);
        return result ? ConsumeOrderlyStatus.SUCCESS : ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
    }
}
