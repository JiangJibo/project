/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.thread;

import org.junit.Test;

/**
 * @since 2017年7月18日 下午2:42:55
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ThreadInterruptTest {

	@Test
	public void testThreadSleep() throws InterruptedException {
		Thread thread = new Thread() {

			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					System.out.println(Thread.currentThread().getName() + "线程在休眠期间被中断");
					Thread.currentThread().interrupt();
				}
			}

		};
		thread.setName("Thread1");
		thread.start();
		Thread.sleep(3000);
		System.out.println("开始中断线程");
		thread.interrupt();
	}

}
