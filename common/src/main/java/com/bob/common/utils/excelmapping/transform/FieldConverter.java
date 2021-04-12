package com.bob.common.utils.excelmapping.transform;

import java.lang.reflect.Field;

/**
 * 将Excel单元格内数据转换为指定属性值
 * S: 单元格内值
 * T: 属性值
 *
 * @author wb-jjb318191
 * @create 2018-01-24 9:20
 */
public interface FieldConverter<S, T> {

    /**
     * 是否支持转换当前属性
     *
     * @param field
     * @return
     */
    boolean support(Field field);

    /**
     * 将原始值转换为目标值
     *
     * @param s
     * @return
     */
    T convert(S s) throws Exception;

}