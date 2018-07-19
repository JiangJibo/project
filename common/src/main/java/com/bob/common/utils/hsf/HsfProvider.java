package com.bob.common.utils.hsf;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HSF组件标识
 *
 * @author wb-jjb318191
 * @create 2017-12-26 12:07
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HsfProvider {

    /**
     * 版本
     *
     * @return
     */
    String serviceVersion() default "";

    /**
     * Service接口名称
     *
     * @return
     */
    String serviceInterface() default "";

    /**
     * 分组
     *
     * @return
     */
    String serviceGroup() default "";

    /**
     * 超时时间
     *
     * @return
     */
    int clientTimeout() default -1;
}