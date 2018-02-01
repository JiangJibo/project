package com.bob.project.utils.mybatis.statement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mybatis的SQL语句供应器
 *
 * @author wb-jjb318191
 * @create 2017-09-08 14:37
 */
public class SqlProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlProvider.class);

    private TableFormatter tableFormat = new HumpToUnderLineFormatter();

    /**
     * 根据Bean对象生成插入SQL语句
     *
     * @param bean
     * @return
     */
    public String insert(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = tableFormat.getTableName(beanClass);
        StringBuilder insertSql = new StringBuilder();
        List<String> columns = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        insertSql.append("INSERT INTO ").append(tableName).append("(");
        try {
            for (Entry<Field, String> entry : tableFormat.getFieldMappings(beanClass).entrySet()) {
                Field field = entry.getKey();
                field.setAccessible(true);
                if (field.get(bean) != null) {
                    columns.add(entry.getValue());
                    values.add("#{" + field.getName() + "}");
                }
            }
        } catch (Exception e) {
            new RuntimeException("get insert sql has exceptoin:" + e);
        }
        int columnSize = columns.size();
        for (int i = 0; i < columnSize; i++) {
            insertSql.append(columns.get(i));
            insertSql.append(i != columnSize - 1 ? "," : ") VALUES(");
        }
        for (int i = 0; i < columnSize; i++) {
            insertSql.append(values.get(i));
            insertSql.append(i != columnSize - 1 ? "," : ")");
        }
        return insertSql.toString();
    }

    /**
     * 根据Bean对象生成更新SQL语句
     *
     * @param bean
     * @return
     */
    public String update(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = tableFormat.getTableName(beanClass);
        StringBuilder updateSql = new StringBuilder();
        updateSql.append(" UPDATE ").append(tableName).append(" SET ");
        try {
            for (Entry<Field, String> entry : tableFormat.getFieldMappings(beanClass).entrySet()) {
                Field field = entry.getKey();
                field.setAccessible(true);
                if (field.get(bean) != null) {
                    updateSql.append(entry.getValue()).append("=#{").append(field.getName()).append("},");
                }
            }
            updateSql.deleteCharAt(updateSql.length() - 1);
        } catch (Exception e) {
            new RuntimeException("get update sql is exceptoin:" + e);
        }
        updateSql.append(" WHERE ").append(tableFormat.getKeyColumnName(beanClass) + " =#{" + tableFormat.getKeyFiledName(beanClass) + "}");
        return updateSql.toString();
    }

    /**
     * 根据Bean对象生成删除SQL语句
     *
     * @param bean
     * @return
     */
    public String delete(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = tableFormat.getTableName(beanClass);
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append(" DELETE FROM ").append(tableName).append(" WHERE  ");
        try {
            for (Entry<Field, String> entry : tableFormat.getFieldMappings(beanClass).entrySet()) {
                Field field = entry.getKey();
                field.setAccessible(true);
                if (field.get(bean) != null) {
                    deleteSql.append(entry.getValue()).append("=#{").append(field.getName()).append("} AND ");
                }
            }
            deleteSql.delete(deleteSql.length() - 5, deleteSql.length() - 1);
        } catch (Exception e) {
            new RuntimeException("get delete sql is exceptoin:" + e);
        }
        return deleteSql.toString();
    }

    /**
     * 生成查询SQL语句
     *
     * @param bean
     * @return
     */
    public String select(Object bean) {
        Class<?> beanClass = bean.getClass();
        String tableName = tableFormat.getTableName(beanClass);
        StringBuilder selectSql = new StringBuilder();
        List<String> columns = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        selectSql.append("SELECT ");
        try {
            for (Entry<Field, String> entry : tableFormat.getFieldMappings(beanClass).entrySet()) {
                Field field = entry.getKey();
                field.setAccessible(true);
                selectSql.append(entry.getValue() + ",");
                if (field.get(bean) != null) {
                    columns.add(entry.getValue());
                    values.add("#{" + field.getName() + "}");
                }
            }
            selectSql.deleteCharAt(selectSql.length() - 1);
        } catch (Exception e) {
            new RuntimeException("get select sql is exceptoin:" + e);
        }
        selectSql.append(" FROM ").append(tableName).append(" WHERE ");
        int columnSize = columns.size();
        for (int i = 0; i < columnSize; i++) {
            selectSql.append(columns.get(i)).append("=").append(values.get(i)).append(" AND ");
        }
        selectSql.delete(selectSql.length() - 5, selectSql.length() - 1);
        return selectSql.toString();
    }

}
