package com.bob.intergrate.rocket.producer.tx;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 当前版本不支持Broker对PREPARED消息的会回查功能了
 *
 * @author wb-jjb318191
 * @create 2018-04-02 15:52
 */
public class DefaultTransactionCheckListener implements TransactionCheckListener {

    @Override
    public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
        return LocalTransactionState.UNKNOW;
    }
}
