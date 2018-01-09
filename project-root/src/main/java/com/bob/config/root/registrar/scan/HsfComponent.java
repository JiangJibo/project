package com.bob.config.root.registrar.scan;

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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HsfComponent {

    /**
     * 是否要将标识此注解的类注册为Spring的Bean
     *
     * @return
     */
    boolean registerBean() default false;

}