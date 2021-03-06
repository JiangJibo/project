package com.bob.common.utils.validate.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.common.utils.validate.Group;
import com.bob.common.utils.validate.Validators;

import static com.bob.common.utils.validate.Group.DEFAULT;

/**
 * 邮箱
 *
 * @author wb-jjb318191
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@TargetValidator(Validators.EMAIL)
public @interface Email {

    /**
     * 分组
     *
     * @return
     */
    Group group() default DEFAULT;

}