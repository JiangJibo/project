package com.bob.common.utils.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据校验
 *
 * @author wb-jjb318191
 * @create 2018-01-31 13:51
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataValidate {

    /**
     * 方法待校验参数,默认第一个
     *
     * @return
     */
    int order() default 0;

    /**
     * 分组
     * {@linkplain Group#DEFAULT}使用于所有分组的校验
     *
     * @return
     */
    Group group() default Group.DEFAULT;

}