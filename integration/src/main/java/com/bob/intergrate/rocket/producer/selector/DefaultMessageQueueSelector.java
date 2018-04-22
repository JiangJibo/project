package com.bob.intergrate.rocket.producer.selector;

import java.util.List;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.util.Assert;

/**
 * 默认的MessageQueue选择器
 *
 * @author wb-jjb318191
 * @create 2018-03-23 16:04
 */
public class DefaultMessageQueueSelector implements MessageQueueSelector {

    @Override
    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
        Assert.isInstanceOf(Selector.class, arg, "非法参数:" + arg.toString());
        switch ((Selector)arg) {
            case FIRST:
                return mqs.get(0);
            case SECOND:
                return mqs.get(1);
            case THIRD:
                return mqs.get(2);
            case LAST:
                return mqs.get(3);
            default:
                throw new IllegalArgumentException("非法参数:" + arg.toString());
        }
    }

    enum Selector {

        FIRST(),
        SECOND(),
        THIRD(),
        LAST();

    }

}
