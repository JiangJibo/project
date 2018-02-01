package com.bob.project.utils.validate.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.project.utils.validate.Group;

import static com.bob.project.utils.validate.Group.DEFAULT;
import static com.bob.project.utils.validate.Validators.MAX_LENGTH;

/**
 * 字符串最大长度
 *
 * @author wb-jjb318191
 * @create 2018-01-31 10:14
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Payload(MAX_LENGTH)
public @interface MaxLength {

    /**
     * 长度
     *
     * @return
     */
    int value();

    /**
     * 分组
     *
     * @return
     */
    Group group() default DEFAULT;

}