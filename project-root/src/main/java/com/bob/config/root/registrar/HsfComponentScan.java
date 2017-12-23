package com.bob.config.root.registrar;

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
 * @author Administrator
 * @create 2017-12-22 23:04
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HsfBeanDefinitionRegistar.class)
public @interface HsfComponentScan {

    /**
     * @return
     */
    String[] basePackages();

    /**
     * @return
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * @return
     */
    boolean registerInternalBeans() default true;

    /**
     * @return
     */
    Filter[] includeFilters() default {};

    /**
     * @return
     */
    Filter[] excludeFilters() default {};

}