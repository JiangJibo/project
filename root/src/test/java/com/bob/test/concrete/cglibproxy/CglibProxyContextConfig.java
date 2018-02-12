/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.cglibproxy;

import java.lang.reflect.Method;

import org.junit.Before;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * CGLIB代理测试配置
 * 
 * @since 2017年8月2日 下午4:14:59
 * @version $Id$
 * @author JiangJibo
 *
 */
public abstract class CglibProxyContextConfig<T> {

	protected T originalBean; // 被代理对象
	protected Class<?> adviceDeclaredScope; // 定义Advice通知的类
	private ProxyFactory proxyFactory;

	@Before
	public void before() {
		init();
		configProxyFactory();
		Assert.notNull(originalBean, "必须指定被代理的对象");
		Assert.notNull(adviceDeclaredScope, "必须指定定义切面通知的Class");
	}

	private void configProxyFactory() {
		proxyFactory = new ProxyFactory();
		proxyFactory.setProxyTargetClass(true);
		proxyFactory.setOptimize(false);
		proxyFactory.setExposeProxy(false);
		proxyFactory.setFrozen(false);
		proxyFactory.setOpaque(false);
	}

	/**
	 * 创建代理对象
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected final T createProxy(Method adviceMethod) throws Exception {
		proxyFactory.addAdvice(buildAdvice(adviceMethod));
		proxyFactory.addAdvisor(0, ExposeInvocationInterceptor.ADVISOR);
		proxyFactory.setTargetSource(new SingletonTargetSource(originalBean));
		return (T) proxyFactory.getProxy(originalBean.getClass().getClassLoader());
	}

	/**
	 * 根据指定方法上的注解信息生成切面通知对象
	 * 
	 * @param adviceMethod
	 * @return
	 * @throws Exception
	 */
	private AbstractAspectJAdvice buildAdvice(Method adviceMethod) throws Exception {
		return AspectjAdviceTye.valueOf(adviceMethod).buildAdvice(adviceMethod, originalBean);
	}

	/**
	 * 查找指定切面类上的通知方法
	 * 
	 * @param name
	 * @param paramTypes
	 * @return
	 */
	protected final Method findAspectMethod(String name, Class<?>... paramTypes) {
		return ReflectionUtils.findMethod(adviceDeclaredScope, name, paramTypes);
	}

	/**
	 * 初始化被代理对象即指定切面通知所在的Class
	 */
	protected abstract void init();

}
