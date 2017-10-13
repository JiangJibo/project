/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.userenv.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @since 2016年12月7日 下午2:01:13
 * @version $Id$
 * @author JiangJibo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface UserEnv {

	/**
	 * 环境变量的名称
	 * 
	 * @return
	 */
	String value() default "";

}
