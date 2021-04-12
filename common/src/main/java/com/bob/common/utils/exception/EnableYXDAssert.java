package com.bob.common.utils.exception;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 运行使用自定义的Assert
 *
 * @author wb-jjb318191
 * @create 2019-09-05 10:55
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ExceptionDefinitionHandler.class)
public @interface EnableYXDAssert {

    /**
     * 扫描路径, 未指定时用当前类路径作为扫描路径
     *
     * @return
     */
    String[] scanPackages() default {};

}