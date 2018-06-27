package com.bob.web.config.aop.advisor;

import com.bob.integrate.mybatis.tx.TransactionAspectInvoker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

/**
 * 事务切面
 *
 * @author wb-jjb318191
 * @create 2017-12-27 13:53
 */
@Aspect
@Order(2)
public class TransactionAspectJAdvisor {

    @Autowired
    private TransactionAspectInvoker transactionAspectInvoker;

    @Around("com.bob.web.config.aop.pointcut.PointcutArchitecture.serviceMethod()")
    public Object invokeWithTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        return transactionAspectInvoker.invoke(joinPoint);
    }

}
