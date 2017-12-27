package com.bob.config.root.tx;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 事务切面执行器
 *
 * @author wb-jjb318191
 * @create 2017-12-27 11:17
 */
public class TransactionAspectInvoker extends TransactionAspectSupport {

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
