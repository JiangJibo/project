/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.web.config.aop.advisor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.bob.web.config.aop.pointcut.PointcutArchitecture;
import com.bob.web.config.userenv.ann.UserEnv;
import com.bob.web.config.userenv.ann.UserEnvAnnotationProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 面向切面的功能实现，对@UserEnv注解的属性使用用户信息填充
 * 
 * @since 2016年12月6日 下午4:52:22
 * @version $Id$
 * @author JiangJibo
 *
 */
@Order(2)
@Aspect
@Component
public class UserEnvAspectJAdvisor {

	final static Logger LOGGER = LoggerFactory.getLogger(UserEnvAspectJAdvisor.class);

	@Autowired
	private UserEnvAnnotationProcessor userEnvAnnotationProcessor;

	/**
	 * Service层注入用户登录信息
	 * 
	 * {@link PointcutArchitecture#serviceMethod}
	 * 
	 * @param joinpoint
	 * @throws Exception
	 */
	@Before("com.bob.web.config.aop.pointcut.PointcutArchitecture.serviceMethod()")
	public void beforeServiceCall(JoinPoint joinpoint) throws Exception {
		Object[] args = joinpoint.getArgs();
		Signature sig = joinpoint.getSignature();
		Method method = ((MethodSignature) sig).getMethod();
		boolean debug = LOGGER.isDebugEnabled();
		if (args != null && args.length != 0) {
			long t1 = System.nanoTime(), t2 = 0;
			boolean injected = false;
			StringBuilder sb = null;
			if (debug) {
				sb = new StringBuilder(1024);
				sb.append("触发Service方法执行前注入, 方法名称: [").append(sig.toLongString()).append("], ");
				sb.append("注入参数列表: ");
			}
			Annotation[][] annotations = method.getParameterAnnotations();
			int i, j;
			for (i = 0; i < annotations.length; i++) {
				Object bean = args[i];
				if (bean == null) {
					continue;
				}
				Annotation[] pa = annotations[i];
				if (pa.length == 0) {
					continue;
				}
				if (UserEnv.class.isInstance(pa[0])) {
					injected = populateEnv(bean, debug, sb);
					continue;
				}
				for (j = 1; j < pa.length; j++) {
					if (UserEnv.class.isInstance(pa[j])) {
						injected = populateEnv(bean, debug, sb);
						break;
					}
				}
			}
			if (debug && injected) {
				t2 = System.nanoTime();
				sb.append("\n完成注入共计耗时: ").append((t2 - t1) / 1000000.0).append("ms");
				LOGGER.debug(sb.toString());
			}
		}

	}

	/**
	 * Service层方法执行过完之后
	 * 
	 * {@link PointcutArchitecture#serviceMethod}
	 * 
	 * @param joinpoint
	 * @param retVal
	 */
	@AfterReturning(pointcut = "com.bob.web.config.aop.pointcut.PointcutArchitecture.serviceMethod()", returning = "retVal")
	public void afterServiceCall(JoinPoint joinpoint, Object retVal) {

	}

	/**
	 * 填充@UserEnv标识的变量
	 * 
	 * @param bean
	 * @param debug
	 * @param sb
	 * @throws Exception
	 * @return
	 */
	private boolean populateEnv(Object bean, boolean debug, StringBuilder sb) throws Exception {
		boolean result = false;
		try {
			result = userEnvAnnotationProcessor.process(bean, sb);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw e;
		}
		return result;
	}

}
