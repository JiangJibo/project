/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.clazz;

/**
 * @since 2017年7月26日 下午3:07:58
 * @version $Id$
 * @author JiangJibo
 *
 */
public class Entity implements Handler {

	/* (non-Javadoc)
	 * @see com.bob.test.concrete.clazz.Handler#handler()
	 */
	@Override
	public void handler() {
		System.out.println("invoke Handler.handler()");
	}

	public class Inner0 {

	}

	interface Inner1 {

	}

	public static class Inner2 {

	}

}
