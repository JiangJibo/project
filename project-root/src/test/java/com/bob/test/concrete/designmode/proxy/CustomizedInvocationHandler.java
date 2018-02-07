/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.designmode.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @since 2017年5月31日 上午9:40:58
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CustomizedInvocationHandler implements InvocationHandler {

	private Object delegate;

	public CustomizedInvocationHandler(Object delegate) {
		super();
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long time1 = System.currentTimeMillis();
		method.invoke(delegate, args);
		long time2 = System.currentTimeMillis();
		System.out.println("执行任务耗时:" + (time2 - time1) + "毫秒");
		return null;
	}

}
