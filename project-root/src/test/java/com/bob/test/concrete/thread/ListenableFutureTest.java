/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.thread;

import java.util.concurrent.Callable;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.bob.test.config.BaseControllerTest;

/**
 * @since 2017年7月18日 上午11:11:06
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ListenableFutureTest extends BaseControllerTest {

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	/* (non-Javadoc)
	 * @see com.bob.test.config.BaseControllerTest#init()
	 */
	@Override
	protected void init() {

	}

	@Test
	public void test0() {
		Callable<String> call = () -> {
			System.out.println(Thread.currentThread().getName());
			throw new InterruptedException("线程中断异常");
			// return "success";
		};
		ListenableFuture<String> future = threadPoolTaskExecutor.submitListenable(call);
		future.addCallback(new ListenableFutureCallback<String>() {

			/* (non-Javadoc)
			 * @see org.springframework.util.concurrent.FailureCallback#onFailure(java.lang.Throwable)
			 */
			@Override
			public void onFailure(Throwable ex) {
				System.out.println("发生了[" + ex.getMessage() + "]异常");
			}

			/* (non-Javadoc)
			 * @see org.springframework.util.concurrent.SuccessCallback#onSuccess(java.lang.Object)
			 */
			@Override
			public void onSuccess(String result) {
				System.out.println("成功执行了回调函数");
			}

		});
		System.out.println("测试ListenableFuture的CallBack方法是否是异步的!");
	}

}
