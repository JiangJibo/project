/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.cglibproxy;

import com.bob.project.config.mvc.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;

/**
 * AOP切面方法定义
 * 
 * @since 2017年8月2日 上午9:23:05
 * @version $Id$
 * @author JiangJibo
 *
 */
public class AspectPointcutConfig {

	/**
	 * 匹配参数含有{@code BankUserGenerator}类型的方法
	 * 
	 * @param joinpoint
	 */
	@Before("args(com.bob.mvc.entity.BankUserGenerator)")
	public void argsOnBefore(JoinPoint joinpoint) {
		Object[] args = joinpoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof User) {
				User user = (User) args[i];
				user.setUserName("PROXY_SET_NAME");
			}
		}
	}

	/**
	 * @param joinpoint
	 */
	@After("execution(public * ProxiedModel.*(..))")
	public void exeOnAfter(JoinPoint joinpoint) {
		System.out.println("在代理方法执行之后执行onAfter()方法");
	}

	/**
	 * @param joinpoint
	 */
	@Before("@args(UserEnv)")
	public void atArgsOnBefore(JoinPoint joinpoint) {
		System.out.println("在代理方法执行之后执行onAfter()方法");
	}

	/**
	 * 匹配标识了指定注解{@code UserEnv} 的方法
	 * 
	 * @param pjp
	 * @throws Throwable
	 */
	@Around("@annotation(UserEnv)")
	public void atAnnOnAround(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("执行Around的前半部分");
		Object result = pjp.proceed();
		System.out.println("执行Around的后半部分,结果为[" + result + "]");
	}

	/**
	 * 被代理类标识了{@code  UserEnv}的类及其子类
	 * 
	 * @param joinpoint
	 * @param result
	 */
	@AfterReturning(value = "@within(org.springframework.context.annotation.Primary)", returning = "result")
	public void atWithinOnAfterReturnning(JoinPoint joinpoint, Object result) {
		System.out.println("atWithinOnAfterReturnning() == " + result);
	}

	/**
	 * 被代理类是this指定的类(接口)或者子类,实现类
	 * 
	 * @param joinpoint
	 * @param ex
	 */
	@AfterThrowing(value = "this(ProxiedModel)", throwing = "ex")
	public void thisOnAfterThrowing(JoinPoint joinpoint, IllegalStateException ex) {
		System.out.println(ex.getMessage());
	}

}
