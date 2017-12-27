package com.bob.config.root.registrar.scan;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;

/**
 * Hsf组件扫描
 *
 * @author wb-jjb318191
 * @create 2017-12-22 23:04
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HsfBeanDefinitionRegistrar.class)
public @interface HsfComponentScan {

    /**
     * @return
     */
    String[] value() default {};

    /**
     * 扫描包
     *
     * @return
     */
    String[] basePackages() default {};

    /**
     * 扫描的基类
     *
     * @return
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * 是否注册HSF基础Bean,默认是;
     * 比如对一个为Service生成HSF,注册Service这个Bean
     *
     * @return
     */
    boolean registerUnderlyingBeans() default true;

    /**
     * 包含过滤器
     *
     * @return
     */
    Filter[] includeFilters() default {};

    /**
     * 排斥过滤器
     *
     * @return
     */
    Filter[] excludeFilters() default {};

}