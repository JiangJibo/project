/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.threadlocal;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.bob.root.config.entity.RootUser;
import org.junit.Test;

/**
 * @since 2017年4月7日 上午9:27:23
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ThreadLocalTest {
	
	private static final CompletionService<RootUser> service = new ExecutorCompletionService<RootUser>(Executors.newFixedThreadPool(5));

	private RootUser u = new RootUser("lanboal", "123456");

	private ThreadLocal<RootUser> threadUser = new ThreadLocal<RootUser>() {

		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected RootUser initialValue() {
			System.out.println(Thread.currentThread().getName() + "线程执行初始化方法");
			return u;
		}

	};

	@Test
	public void testThreadLocal() throws InterruptedException, ExecutionException, TimeoutException {
		Callable<RootUser> call1 = () -> {
			Thread.currentThread().setName("线程1");
			// threadUser.set(new BankUserGenerator("bob", "123456"));
			System.out.println(threadUser.toString());
			return threadUser.get();
		};

		Callable<RootUser> call2 = () -> {
			Thread.currentThread().setName("线程2");
			System.out.println(threadUser.toString());
			return threadUser.get();
		};

		service.submit(call1);
		service.submit(call2);
		RootUser user1 = service.take().get(1000, TimeUnit.MILLISECONDS);
		RootUser user2 = service.take().get(1000, TimeUnit.MILLISECONDS);

		System.out.println(user1.toString() + "\t" + user1.getName());
		System.out.println(user2.toString() + "\t" + user2.getName());
	}

}
