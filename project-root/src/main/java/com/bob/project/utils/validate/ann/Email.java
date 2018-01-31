package com.bob.project.utils.validate.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.bob.project.utils.validate.Validator.EMAIL;

/**
 * 邮箱
 *
 * @author wb-jjb318191
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Payload(EMAIL)
public @interface Email {

    /**
     * 是否为空
     *
     * @return
     */
    boolean notNull() default false;

    /**
     * 属性名称
     *
     * @return
     */
    String name();
}