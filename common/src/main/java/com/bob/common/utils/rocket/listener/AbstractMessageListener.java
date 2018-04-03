package com.bob.common.utils.rocket.listener;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;

import com.bob.common.utils.rocket.ann.RocketListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * RocketMQ消息监听器
 *
 * @author wb-jjb318191
 * @create 2018-04-02 16:26
 */
public abstract class AbstractMessageListener {

    protected Object consumeBean;
    protected Method consumeMethod;
    private static final String ERROR_MSG_PREFIX = "[@RocketListener]标识的方法";

    public AbstractMessageListener(Object consumeBean, Method consumeMethod) {
        this.consumeBean = consumeBean;
        this.consumeMethod = consumeMethod;
        this.checkConsumeMethod();
    }

    protected Object[] buildConsumeArguments(List<MessageExt> msgs, Object context) {
        Class<?>[] types = consumeMethod.getParameterTypes();
        Object[] args = new Object[types.length];
        if (types[0] == List.class) {
            args[0] = msgs;
        } else {
            args[0] = msgs.get(0);
        }
        if (types.length == 2) {
            args[1] = context;
        }
        return args;
    }

    /**
     * 校验{@link RocketListener}标识的方法正确性
     */
    protected void checkConsumeMethod() {
        RocketListener listener = consumeMethod.getAnnotation(RocketListener.class);
        Assert.state(listener != null, String.format("RocketMQ消费者方法必须标识[@%s]注解", ClassUtils.getShortName(RocketListener.class)));
        //校验方法参数
        int paramLength = consumeMethod.getParameterCount();
        Assert.state(paramLength == 1 || paramLength == 2, ERROR_MSG_PREFIX + "参数长度只能在1和2之间");
        Parameter param0 = consumeMethod.getParameters()[0];
        boolean isMsg = MessageExt.class.isAssignableFrom(param0.getType());
        boolean isList = List.class.isAssignableFrom(param0.getType());
        Assert.state(isMsg || isList, ERROR_MSG_PREFIX + "第一个参数只能是MessageExt或List<MessageExt>类型");
        if (isList) {
            Class<?> generic = ResolvableType.forMethodParameter(consumeMethod, 0).resolveGeneric(0);
            Assert.isAssignable(MessageExt.class, generic, ERROR_MSG_PREFIX + "第一个参数的泛型只能是MessageExt类型");
        }
        if (paramLength == 2) {
            if (listener.orderly()) {
                Assert.state(consumeMethod.getParameters()[1].getType() == ConsumeOrderlyContext.class,
                    ERROR_MSG_PREFIX + "有序消费时第2个参数类型只能是[ConsumeOrderlyContext]");
            } else {
                Assert.state(consumeMethod.getParameters()[1].getType() == ConsumeConcurrentlyContext.class,
                    ERROR_MSG_PREFIX + "并发消费时第2个参数类型只能是[ConsumeConcurrentlyContext]");
            }
        }
        Assert.state(Modifier.isPublic(consumeMethod.getModifiers()), ERROR_MSG_PREFIX + "修饰符必须为[Public]");
        Assert.state(consumeMethod.getReturnType() == boolean.class, ERROR_MSG_PREFIX + "返回值类型必须为[boolean]");
    }

}
