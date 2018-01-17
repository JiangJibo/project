package com.bob.config.root.tx;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 事务切面执行器
 *
 * @author wb-jjb318191
 * @create 2017-12-27 11:17
 */
public class TransactionAspectInvoker extends TransactionAspectSupport {

    public TransactionAspectInvoker() {
        //设置事务的属性
        DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
        //不设置事务的隔离级别,默认使用数据库的设置
        //transactionAttribute.setIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ);
        //设置事务的超时时间,单位秒,注意是事务不是连接的超时设置
        transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionAttribute.setTimeout(300);
        //设置事务源
        MatchAlwaysTransactionAttributeSource txAttrBs = new MatchAlwaysTransactionAttributeSource();
        txAttrBs.setTransactionAttribute(transactionAttribute);
        setTransactionAttributeSource(txAttrBs);
        //可以配置多个事务属性源,对不同的方法生成不同的事务属性
        //txInvoker.setTransactionAttributeSources(new TransactionAttributeSource[] {});
    }

    /**
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object invoke(final ProceedingJoinPoint joinPoint) throws Throwable {
        // Work out the target class: may be {@code null}.
        // The TransactionAttributeSource should be passed the target class
        // as well as the method, which may be from an interface.
        Class<?> targetClass = (joinPoint.getThis() != null ? AopUtils.getTargetClass(joinPoint.getThis()) : null);

        // Adapt to TransactionAspectSupport's invokeWithinTransaction...
        return invokeWithinTransaction(((MethodSignature)joinPoint.getSignature()).getMethod(), targetClass, () -> joinPoint.proceed());
    }
}
