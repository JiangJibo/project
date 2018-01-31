package com.bob.project.utils.validate.ann;

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
     * 从所有待验证属性中选取这些
     *
     * @return
     */
    String[] include() default {};

    /**
     * 不包含待验证属性名称,从所有属性中剔除当前属性
     *
     * @return
     */
    String[] exclude() default {};

    /**
     * 分组
     *
     * @return
     */
    Group group() default DEFAULT;

}