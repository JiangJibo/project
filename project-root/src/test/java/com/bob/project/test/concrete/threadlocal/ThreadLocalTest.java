/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.threadlocal;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.bob.project.config.mvc.model.User;
import org.junit.Test;

/**
 * @since 2017年4月7日 上午9:27:23
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ThreadLocalTest {
	
	private static final CompletionService<User> service = new ExecutorCompletionService<User>(Executors.newFixedThreadPool(5));

	private User u = new User("lanboal", "123456");

	private ThreadLocal<User> threadUser = new ThreadLocal<User>() {

		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected User initialValue() {
			System.out.println(Thread.currentThread().getName() + "线程执行初始化方法");
			return u;
		}

	};

	@Test
	public void testThreadLocal() throws InterruptedException, ExecutionException, TimeoutException {
		Callable<User> call1 = () -> {
			Thread.currentThread().setName("线程1");
			// threadUser.set(new BankUserGenerator("bob", "123456"));
			System.out.println(threadUser.toString());
			return threadUser.get();
		};

		Callable<User> call2 = () -> {
			Thread.currentThread().setName("线程2");
			System.out.println(threadUser.toString());
			return threadUser.get();
		};

		service.submit(call1);
		service.submit(call2);
		User user1 = service.take().get(1000, TimeUnit.MILLISECONDS);
		User user2 = service.take().get(1000, TimeUnit.MILLISECONDS);

		System.out.println(user1.toString() + "\t" + user1.getUserName());
		System.out.println(user2.toString() + "\t" + user2.getUserName());
	}

}
