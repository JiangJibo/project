/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.clazz;

/**
 * @since 2017年8月6日 下午3:06:14
 * @version $Id$
 * @author JiangJibo
 *
 */
public interface Handler {

	void handler();

	default void defaultMethod(){
		System.out.println("This is a Default Method");
	}

}
