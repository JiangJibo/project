package com.bob.common.utils.hsf;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HSF组件标识
 *
 * @author wb-jjb318191
 * @create 2017-12-26 12:07
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HsfComponent {

    /**
     * 是否要将标识此注解的类注册为Spring的Bean
     *
     * @return
     */
    boolean registerBean() default false;

    /**
     * 版本号
     *
     * @return
     */
    String serviceVersion() default "";

    /**
     * 分组
     *
     * @return
     */
    String serviceGroup() default "";

    /**
     * 客户端过期时间
     *
     * @return
     */
    String clientTimeout() default "";

}