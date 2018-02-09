package com.bob.web.config.aop.pointcut;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 系统架构定义切入点
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月7日 上午10:54:28
 */
@Order(1)
@Aspect
@Component
public class PointcutArchitecture {

    /**
     * 面向Service层的切入点
     */
    @Pointcut("execution(public * com.bob.web.mvc.service..*(..))")
    public void serviceMethod() {
    }

    /**
     * 面向{@code UserEnv }注解的切入点
     */
    @Pointcut("@args(com.bob.web.config.userenv.ann.UserEnv)")
    public void injectMethod() {
    }

}
