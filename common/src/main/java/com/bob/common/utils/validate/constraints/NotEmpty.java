package com.bob.common.utils.validate.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.common.utils.validate.Group;

import static com.bob.common.utils.validate.Validators.NOT_EMPTY;

/**
 * 内部非空
 *
 * @author wb-jjb318191
 * @create 2018-01-31 10:13
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@TargetValidator(NOT_EMPTY)
public @interface NotEmpty {

    /**
     * 分组
     *
     * @return
     */
    Group group() default Group.DEFAULT;

}