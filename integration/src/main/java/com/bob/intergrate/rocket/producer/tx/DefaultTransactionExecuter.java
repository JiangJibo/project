package com.bob.intergrate.rocket.producer.tx;

import java.lang.reflect.Type;

import com.bob.root.utils.model.RootUser;
import com.google.gson.Gson;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;

import static org.apache.rocketmq.client.producer.LocalTransactionState.COMMIT_MESSAGE;
import static org.apache.rocketmq.client.producer.LocalTransactionState.ROLLBACK_MESSAGE;
import static org.apache.rocketmq.client.producer.LocalTransactionState.UNKNOW;

/**
 * 默认事务执行器
 *
 * @author wb-jjb318191
 * @create 2018-04-02 10:34
 */
public class DefaultTransactionExecuter implements LocalTransactionExecuter {

    private static final Gson GSON = new Gson();

    @Override
    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
        Object body = GSON.fromJson(new String(msg.getBody()), (Type)arg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        if (body instanceof RootUser) {
            int id = ((RootUser)body).getId();
            return id % 2 == 0 ? COMMIT_MESSAGE : ROLLBACK_MESSAGE;
        }
        return UNKNOW;
    }
}
