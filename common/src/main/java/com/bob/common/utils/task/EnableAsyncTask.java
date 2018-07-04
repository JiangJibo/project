package com.bob.common.utils.task;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 启用异步任务调度器
 *
 * @author wb-jjb318191
 * @create 2018-07-04 9:23
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AsyncTaskDispatcher.class)
public @interface EnableAsyncTask {
}