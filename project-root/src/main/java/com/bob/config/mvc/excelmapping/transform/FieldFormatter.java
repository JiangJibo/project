package com.bob.config.mvc.excelmapping.transform;

import java.lang.reflect.Field;

/**
 * 将属性格式化为指定类型数据串输出到Excel
 * S: 属性值
 * T: 单元格内输出值
 *
 * @author wb-jjb318191
 * @create 2018-01-23 13:41
 */
public interface FieldFormatter<S, T> {

    /**
     * 是否支持对当前属性值的格式化
     *
     * @param field
     * @param value
     * @return
     */
    boolean support(Field field, Object value);

    /**
     * 将当前类型的对象格式化为字符串
     *
     * @param s
     * @return
     */
    T format(S s);

}