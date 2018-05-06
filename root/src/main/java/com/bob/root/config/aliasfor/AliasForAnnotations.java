package com.bob.root.config.aliasfor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.type.filter.TypeFilter;

/**
 * 测试组合注解
 *
 * @author Administrator
 * @create 2018-05-03 20:21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@ScanFilter
@ScanPackages
public @interface AliasForAnnotations {

    /**
     * @return
     */
    @AliasFor(annotation = ScanPackages.class, attribute = "packages")
    String[] packages() default {};

    /**
     * @return
     */
    @AliasFor(annotation = ScanPackages.class, attribute = "packageClasses")
    Class[] packageClasses() default {};

    /**
     * @return
     */
    @AliasFor(annotation = ScanFilter.class, attribute = "filterClass")
    Class<? extends TypeFilter> filterClass() default TypeFilter.class;

}