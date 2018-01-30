/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.thread;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 2017年7月18日 下午3:21:43
 * @version $Id$
 * @author JiangJibo
 *
 */
public class LockMechanismTest {

	final static Logger LOGGER = LoggerFactory.getLogger(LockMechanismTest.class);

	private static String getThreadName() {
		return Thread.currentThread().getName();
	}

	public static synchronized void staticSyncMethod() throws InterruptedException {
		Thread.sleep(3000);
		LOGGER.debug(getThreadName() + "静态同步方法执行");
	}

	public synchronized void syncMethod() throws InterruptedException {
		Thread.sleep(3000);
		LOGGER.debug(getThreadName() + "非静态同步方法执行");
	}

	public void nonSyncMethod() throws InterruptedException {
		synchronized (this) {
			Thread.sleep(3000);
			LOGGER.debug(getThreadName() + "同步this的非静态方法执行");
		}
	}

	@Test
	public void teststaticSyncMethod() throws InterruptedException {
		LockMechanismTest lockTest = new LockMechanismTest();
		for (int i = 0; i < 3; i++) {
			Thread thread = new Thread() {

				public void run() {
					try {
						// static synchronized方法使用的是Class对象作为锁,多个static synchronized
						// 方法同一时间只有一个能运行,不是对象锁
						LockMechanismTest.staticSyncMethod();
					} catch (InterruptedException e) {
						LOGGER.debug("线程在休眠期间被中断");
						Thread.currentThread().interrupt();
					}
				}

			};
			thread.setName("线程" + i);
			thread.start();
		}
		lockTest.nonSyncMethod();
		lockTest.syncMethod();
		Thread.sleep(15000);
	}

	@Test
	public void testsyncMethod() throws InterruptedException {
		LockMechanismTest lockTest = new LockMechanismTest();
		for (int i = 0; i < 3; i++) {
			Thread thread = new Thread() {

				public void run() {
					try {
						lockTest.syncMethod();
					} catch (InterruptedException e) {
						LOGGER.debug("线程在休眠期间被中断");
						Thread.currentThread().interrupt();
					}
				}

			};
			thread.setName("线程" + i);
			thread.start();
		}
		// synchronized(this)和synchronized方法使用的是同一个锁,即调用此方法的对象,多个这些方法同一时间只有一个能运行
		lockTest.nonSyncMethod();
		Thread.sleep(10000);
	}

}
