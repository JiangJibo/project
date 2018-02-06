/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.config.registrar;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * @since 2017年1月19日 上午9:14:57
 * @version $Id$
 * @author JiangJibo
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ImportedBeanRegistrar.class)
public @interface ImportedBeanRegistry {

	int id() default 001;

	String value();

	String telephone();

	int age() default 28;

	String adress() default "杭州";
}
