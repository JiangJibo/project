package com.bob.project.utils.validate.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.project.utils.validate.Group;

import static com.bob.project.utils.validate.Group.DEFAULT;
import static com.bob.project.utils.validate.Validators.EMAIL;

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
     * 分组
     *
     * @return
     */
    Group group() default DEFAULT;

}