package com.bob.project.config.mvc.excelmapping;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标识在Model类上的注解，指明标题栏，起始栏及所在页码
 *
 * @author wb-jjb318191
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface ExcelMapping {

    /**
     * 标识数据在第几页
     *
     * @return
     */
    int sheetAt() default 0;

    /**
     * 数据行第一行位置
     *
     * @return
     */
    int dataRow();

    /**
     * 标题行所在位置,字段说明所在行
     *
     * @return
     */
    int titleRow();
}