/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.web.config.exception;

/**
 * 自定义异常,在合适的场合可以抛出
 * 
 * @since 2017年3月21日 下午6:56:39
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CustomizedException extends RuntimeException {

	private static final long serialVersionUID = -6372945904146943934L;

	public CustomizedException() {
		super();
	}

	public CustomizedException(String message) {
		super(message);
	}

	public CustomizedException(Throwable e) {
		super(e);
	}

	public CustomizedException(String message, Throwable cause) {
		super(message, cause);
	}

}
