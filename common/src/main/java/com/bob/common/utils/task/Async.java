package com.bob.common.utils.task;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异步任务标识注解
 *
 * @author wb-jjb318191
 * @create 2018-07-04 9:18
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {

    /**
     * 任务名称
     *
     * @return
     */
    String taskName();

    /**
     * 任务类型
     *
     * @return
     */
    String taskType();

    /**
     * 异步任务超时时间
     *
     * @return
     */
    int timeout() default 6000;

    /**
     * 流程处理器的Bean名称
     *
     * @return
     */
    String processBeanName() default "";

}