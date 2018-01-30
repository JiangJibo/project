/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.designmode.clone;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @since 2017年6月22日 下午12:02:02
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CloneTest {

	private long time0;
	CloneEntity demo0;
	CloneEntity demo1;

	@Before
	public void processBefore() throws CloneNotSupportedException {
		time0 = System.currentTimeMillis();
		demo0 = new CloneEntity();

	}

	@After
	public void processAfter() {
		System.out.println("总耗时:" + (System.currentTimeMillis() - time0));
		System.out.println(demo0.toString() + " = " + demo1.toString());
		System.out.println(demo0.getUser().equals(demo1.getUser()));
	}

	/**
	 * 测试浅拷贝
	 * 
	 * @throws CloneNotSupportedException
	 */
	@Test
	public void testShallowClone() throws CloneNotSupportedException {
		demo1 = (CloneEntity) demo0.clone();
	}

	/**
	 * 测试深拷贝
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void testDeepCloneByStream() throws ClassNotFoundException, IOException {
		demo1 = demo0.deepCloneByStream();
	}

	/**
	 * 测试深拷贝
	 */
	@Test
	public void testDeepCloneByGson() {
		demo1 = demo0.deepCloneByGson();
	}

}
