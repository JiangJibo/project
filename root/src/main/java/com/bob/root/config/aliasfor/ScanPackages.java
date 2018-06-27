package com.bob.root.config.aliasfor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Administrator
 * @create 2018-05-03 20:17
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ScanPackages {

    /**
     * 包名
     *
     * @return
     */
    String[] packages() default {};

    /**
     * 基础类
     *
     * @return
     */
    Class[] packageClasses() default {};

}