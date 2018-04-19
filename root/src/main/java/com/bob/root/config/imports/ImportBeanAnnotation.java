package com.bob.root.config.imports;

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
@Import(DefaultImportBeanRegistrar.class)
public @interface ImportBeanAnnotation {

	int id() default 001;

	String value();

	String telephone();

	int age() default 28;

	String adress() default "杭州";
}
