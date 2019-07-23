package com.bob.common.utils.request.post.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性映射
 *
 * @author wb-jjb318191
 * @create 2019-07-18 17:36
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface FieldMapping {

    /**
     * 将Http Body里的键值对映射到当前Field
     *
     * @return
     */
    String value() default "";

}