/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.thread;

import java.util.concurrent.Semaphore;

import org.junit.Test;

/**
 * @since 2017年8月20日 下午7:41:22
 * @version $Id$
 * @author JiangJibo
 *
 */
public class SemaphoreExample {

	private static Semaphore semaphore = new Semaphore(2);
	private String lock = "lock";
	private static int count = 0;

	static class MyThread extends Thread {

		@Override
		public void run() {
			super.run();
			try {
				while (true) {
					semaphore.acquire();
					Thread.sleep(500);
					System.out.println(Thread.currentThread().getName() + "get the lock success and run the syn code " + count++);
					semaphore.release();
					Thread.sleep(500);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testSemaphore() throws InterruptedException {
		MyThread thread1 = new MyThread();
		MyThread thread2 = new MyThread();
		MyThread thread3 = new MyThread();
		thread1.start();
		thread2.start();
		thread3.start();
		Thread.sleep(10000);
	}

}
