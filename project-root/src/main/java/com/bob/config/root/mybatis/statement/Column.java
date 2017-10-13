package com.bob.config.root.mybatis.statement;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类对应的列
 *
 * @author wb-jjb318191
 * @create 2017-09-08 14:42
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Column {

    /**
     * 当前属性对应的列名
     *
     * @return
     */
    String value() default "";

    /**
     * 当前属性是不是表必须的
     *
     * @return
     */
    boolean required() default true;

}