package com.bob.project.config.root.mybatis.statement;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类对应的表
 *
 * @author wb-jjb318191
 * @create 2017-09-08 14:44
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {

    String value() default "";

    /**
     * 当前表的主键
     *
     * @return
     */
    String key();

}