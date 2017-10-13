package com.bob.config.root.mybatis.statement;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 表到实体的格式化器
 *
 * @author wb-jjb318191
 * @create 2017-09-08 14:51
 */
public interface TableFormatter {

    /**
     * 根据属性获取字段名称
     *
     * @param field
     * @return
     */
    public String getColumnName(Field field);

    /**
     * 获取主键属性对应的列名
     *
     * @return
     */
    public String getKeyColumnName(Class<?> clazz);

    /**
     * 获取主键的属性名称
     *
     * @param clazz
     * @return
     */
    public String getKeyFiledName(Class<?> clazz);

    /**
     * 根据类获取表名称
     *
     * @param clazz
     * @return
     */
    public String getTableName(Class<?> clazz);

    /**
     * 获取一个类的所有属性的映射信息
     *
     * @param clazz
     * @return
     */
    public Map<Field, String> getFieldMappings(Class<?> clazz);

}