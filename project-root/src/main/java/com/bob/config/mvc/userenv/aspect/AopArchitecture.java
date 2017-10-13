/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.userenv.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 系统架构定义切入点
 * 
 * @since 2016年12月7日 上午10:54:28
 * @version $Id$
 * @author JiangJibo
 *
 */
@Order(1)
@Aspect
@Component
public class AopArchitecture {

	/**
	 * 面向Service层的切入点
	 */
	@Pointcut("execution(public * com.bob.config.mvc.service.*.*(..))")
	public void serviceMethod() {
	}

	/**
	 * 面向{@code UserEnv }注解的切入点
	 */
	@Pointcut("@args(com.bob.config.mvc.userenv.ann.UserEnv)")
	public void injectMethod() {
	}

}
