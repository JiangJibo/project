package com.bob.config.mvc.excelmapping;

/**
 * 属性格式化器,用户将属性格式化为字符串输出到Excel
 *
 * @author wb-jjb318191
 * @create 2018-01-23 13:41
 */
public interface FieldFormatter<S> {

    /**
     * 是否支持对当前值的格式化
     *
     * @param obj
     * @return
     */
    boolean support(Object obj);

    /**
     * 将当前类型的对象格式化为字符串
     *
     * @param s
     * @return
     */
    String format(S s);

}