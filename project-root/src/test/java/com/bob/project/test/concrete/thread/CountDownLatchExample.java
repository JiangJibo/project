/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.thread;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * @since 2017年8月20日 下午7:30:14
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CountDownLatchExample {

	private static CountDownLatch mCountDownLatch = new CountDownLatch(3);

	static class MyThread extends Thread {

		int awaitTime;

		public MyThread(int i) {
			this.awaitTime = i;
		}

		@Override
		public void run() {
			super.run();
			try {
				while (true) {
					Thread.sleep(awaitTime);
					System.out.println(Thread.currentThread().getName() + "arrived ");
					mCountDownLatch.countDown();
					mCountDownLatch.await(); // 可以指定等待时间
					System.out.println(Thread.currentThread().getName() + "start meeting ");
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testCountDownLatch() throws InterruptedException {
		MyThread thread1 = new MyThread(500);
		MyThread thread2 = new MyThread(1000);
		MyThread thread3 = new MyThread(2000);
		thread1.start();
		thread2.start();
		thread3.start();
		Thread.sleep(10000);
	}

}
