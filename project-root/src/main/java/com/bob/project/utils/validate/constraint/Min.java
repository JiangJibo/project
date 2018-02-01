package com.bob.project.utils.validate.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.project.utils.validate.Group;

import static com.bob.project.utils.validate.Group.DEFAULT;
import static com.bob.project.utils.validate.Validators.MIN;

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

    /**
     * 分组
     *
     * @return
     */
    Group group() default DEFAULT;
}