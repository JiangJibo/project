/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.annotation;

import java.lang.annotation.Annotation;

/**
 * @since 2017年8月4日 上午11:06:02
 * @version $Id$
 * @author JiangJibo
 *
 */
public class NonTypeBasedAnnotation implements Annotation {

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return null;
	}

}
