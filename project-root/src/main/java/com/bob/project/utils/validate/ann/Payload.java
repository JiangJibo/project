package com.bob.project.utils.validate.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.project.utils.validate.Validator;

/**
 * 负载处理
 *
 * @author wb-jjb318191
 * @create 2018-01-31 10:16
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Payload {

    /**
     * 处理器枚举
     *
     * @return
     */
    Validator value();
}