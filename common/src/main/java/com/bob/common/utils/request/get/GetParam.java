package com.bob.common.utils.request.get;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * get请求参数
 *
 * @author wb-jjb318191
 * @create 2019-07-18 17:36
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetParam {

    /**
     * 参数名称, 未指定时用属性名称
     *
     * @return
     */
    String value() default "";

}