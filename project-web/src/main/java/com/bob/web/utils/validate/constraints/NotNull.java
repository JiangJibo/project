package com.bob.web.utils.validate.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.web.utils.validate.Group;
import com.bob.web.utils.validate.Validators;

/**
 * 非空
 *
 * @author wb-jjb318191
 * @create 2018-01-31 10:12
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@TargetValidator(Validators.NOT_NULL)
public @interface NotNull {

    /**
     * 分组
     *
     * @return
     */
    Group group() default Group.DEFAULT;

}