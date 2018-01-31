package com.bob.project.utils.validate.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.bob.project.utils.validate.Validator.MIN;

/**
 * 最小值
 *
 * @author wb-jjb318191
 * @create 2018-01-31 10:13
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Payload(MIN)
public @interface Min {

    /**
     * 最小值
     *
     * @return
     */
    long value();
}