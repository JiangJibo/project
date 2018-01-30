package com.bob.project.config.root.tx;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * 事务属性生成器
 *
 * @author Administrator
 * @create 2018-01-17 19:45
 */
public class TransactionAttributeGenerator implements TransactionAttributeSource {

    private static final List<String> WRITE_KEY_WORDS = Arrays.asList("insert", "create", "delete", "remove", "modify", "update", "change");

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
        //默认当前方法是读操作
        transactionAttribute.setReadOnly(true);
        //不设置事务的隔离级别,默认使用数据库的设置
        //transactionAttribute.setIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ);
        //设置事务的超时时间,单位秒,注意是事务不是连接的超时设置
        //transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionAttribute.setTimeout(300);

        //若方法或者类上标识了@WriteManipulation，则指定是写操作
        if (method.isAnnotationPresent(WriteManipulation.class) || targetClass.isAnnotationPresent(WriteManipulation.class)) {
            transactionAttribute.setReadOnly(false);
            return transactionAttribute;
        }
        //若方法上还有写操作的关键字,则指定是写操作
        String methodName = method.getName().toLowerCase();
        for (String keyword : WRITE_KEY_WORDS) {
            if (methodName.contains(keyword)) {
                transactionAttribute.setReadOnly(false);
                break;
            }
        }
        return transactionAttribute;
    }
}
