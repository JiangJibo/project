package com.bob.project.utils.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.project.utils.validate.Group;

import static com.bob.project.utils.validate.Group.DEFAULT;

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
     * 分组,独立的分组{@linkplain Group#A}，{@linkplain Group#B}包含{@linkplain Group#DEFAULT}
     * {@linkplain Group#A}不包含显示的指定为{@linkplain Group#B}的验证条目
     *
     * @return
     */
    Group group() default DEFAULT;

}