package com.bob.root.config.aliasfor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.type.filter.TypeFilter;

/**
 * @author Administrator
 * @create 2018-05-03 20:19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ScanFilter {

    Class<? extends TypeFilter> filterClass() default TypeFilter.class;

}