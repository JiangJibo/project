package com.bob.common.utils.userenv.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.common.utils.userenv.AppUserContextConfig;
import org.springframework.context.annotation.Import;

/**
 * 允许用户属性注入处理
 *
 * @author Administrator
 * @create 2018-03-27 19:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(AppUserContextConfig.class)
public @interface EnableUserEnv {
}