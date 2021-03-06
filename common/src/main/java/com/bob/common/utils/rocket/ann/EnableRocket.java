package com.bob.common.utils.rocket.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.common.utils.rocket.RocketBootstrapConfiguration;
import org.springframework.context.annotation.Import;

/**
 * RocketMQ整合注解
 *
 * @author wb-jjb318191
 * @create 2018-03-20 9:23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Import(RocketBootstrapConfiguration.class)
public @interface EnableRocket {
}