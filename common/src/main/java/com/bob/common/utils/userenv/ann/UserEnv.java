package com.bob.common.utils.userenv.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月7日 下午2:01:13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface UserEnv {

    /**
     * 环境变量的名称
     *
     * @return
     */
    String value() default "";

}
