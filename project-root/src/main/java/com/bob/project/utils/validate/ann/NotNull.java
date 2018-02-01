package com.bob.project.utils.validate.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.project.utils.validate.Group;

import static com.bob.project.utils.validate.Group.DEFAULT;
import static com.bob.project.utils.validate.Validators.NOT_NULL;

/**
 * 非空
 *
 * @author wb-jjb318191
 * @create 2018-01-31 10:12
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Payload(NOT_NULL)
public @interface NotNull {

    /**
     * 分组
     *
     * @return
     */
    Group group() default DEFAULT;

}