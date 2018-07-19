package com.bob.common.utils.hsf;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 启用HSF组件
 *
 * @author wb-jjb318191
 * @create 2018-05-24 9:24
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({HsfConfigImporter.class, HsfBeanDefinitionRegistryPostProcessor.class})
public @interface EnableHSF {

    /**
     * 版本
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
     * 超时时间
     *
     * @return
     */
    String clientTimeout() default "";
}