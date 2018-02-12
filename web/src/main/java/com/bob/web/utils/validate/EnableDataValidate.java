package com.bob.web.utils.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 是否开启数据校验开关
 *
 * @author wb-jjb318191
 * @create 2018-01-31 16:59
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({DataValidateAdvisor.class, ValidatePostProcessor.class})
public @interface EnableDataValidate {
}