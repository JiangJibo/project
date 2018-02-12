/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.designmode.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.beans.BeanUtils;

/**
 * 获取动态代理实例的
 * 
 * @since 2017年5月31日 上午10:11:03
 * @version $Id$
 * @author JiangJibo
 *
 */
public abstract class DynProxyFactory {

	@SuppressWarnings("unchecked")
	public static <T> T getSubjectInstance(Class<? extends T> clazz) {
		T t = BeanUtils.instantiate(clazz);
		InvocationHandler invocationHandler = new CustomizedInvocationHandler(t);
		return (T) Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), invocationHandler);
	}

}
