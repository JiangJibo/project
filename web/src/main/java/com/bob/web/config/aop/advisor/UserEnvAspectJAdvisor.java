package com.bob.web.config.aop.advisor;

import com.bob.common.utils.userenv.process.UserEnvAnnotationProcessor;
import com.bob.web.config.aop.pointcut.PointcutArchitecture;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

/**
 * 面向切面的功能实现，对@UserEnv注解的属性使用用户信息填充
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月6日 下午4:52:22
 */
@Order(2)
@Aspect
public class UserEnvAspectJAdvisor {

    @Autowired
    private UserEnvAnnotationProcessor userEnvAnnotationProcessor;

    /**
     * Service层注入用户登录信息
     *
     * @param joinpoint
     * @throws Exception
     * @see PointcutArchitecture#serviceMethod()
     */
    @Before("com.bob.web.config.aop.pointcut.PointcutArchitecture.serviceMethod()")
    public void beforeServiceCall(JoinPoint joinpoint) throws Exception {
        userEnvAnnotationProcessor.process(joinpoint);
    }

}
