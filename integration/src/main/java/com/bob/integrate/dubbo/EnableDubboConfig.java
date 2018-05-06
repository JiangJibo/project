package com.bob.integrate.dubbo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * EnableDubbo的封装
 *
 * @author wb-jjb318191
 * @create 2018-05-04 10:40
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DubboContextConfigSelector.class)
public @interface EnableDubboConfig {

    /**
     * 指定应用类型
     *
     * @return
     */
    APPLICATION application();

    enum APPLICATION {
        CONSUMER(),
        PROVIDER();
    }

}