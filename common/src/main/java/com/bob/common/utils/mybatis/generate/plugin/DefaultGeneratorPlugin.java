package com.bob.common.utils.mybatis.generate.plugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import static com.bob.common.utils.mybatis.generate.constant.GenerateContextConfig.appendJavaModelDoSuffix;
import static java.sql.Types.BIT;

/**
 * 逆向工程插件
 *
 * @author wb-jjb318191
 * @create 2019-07-18 15:17
 */
public class DefaultGeneratorPlugin extends PluginAdapter {

    private static Map<String, IntrospectedTable> tableConfigMappings = new HashMap<>();

    @Override
    public void initialized(IntrospectedTable introspectedTable) {

        removePropertyIsPrefix(introspectedTable);

        if (appendJavaModelDoSuffix) {
            appendJavaModelDOSuffix(introspectedTable);
        }
        tableConfigMappings.put(convertTableToClassName(introspectedTable.getTableConfiguration().getTableName()), introspectedTable);
    }

    /**
     * 移除Boolean类型的属性名称的is前缀
     *
     * @param introspectedTable
     */
    private void removePropertyIsPrefix(IntrospectedTable introspectedTable) {
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            String javaProperty = column.getJavaProperty();
            if (javaProperty.startsWith("is") && column.getJdbcType() == BIT) {
                javaProperty = (javaProperty.charAt(2) + "").toLowerCase() + javaProperty.substring(3);
                column.setJavaProperty(javaProperty);
            }
        }
    }

    /**
     * 给Model对象加上DO
     *
     * @param introspectedTable
     */
    private void appendJavaModelDOSuffix(IntrospectedTable introspectedTable) {
        Field field = ReflectionUtils.findField(IntrospectedTable.class, "internalAttributes");
        field.setAccessible(true);
        Map internalAttributes = (Map)ReflectionUtils.getField(field, introspectedTable);
        for (Object element : internalAttributes.entrySet()) {
            Entry entry = (Entry)element;
            String value = (String)entry.getValue();
            if ("ATTR_BASE_RECORD_TYPE".equals(entry.getKey().toString())) {
                entry.setValue(value + "DO");
            }
        }
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 获取逆向工程是内省的表配置
     *
     * @param modelPath
     * @return
     */
    public static IntrospectedTable getIntrospectedTable(String modelPath) {
        String key = modelPath.substring(modelPath.lastIndexOf(".")+1);
        if (key.endsWith("DO")) {
            key = key.substring(0, key.length() - 2);
        }
        return tableConfigMappings.get(key);
    }

    /**
     * 依据驼峰原则格式化将表名转换为类名,当遇到下划线时去除下划线并对之后的一位字符大写
     *
     * @param table
     * @return
     */
    private String convertTableToClassName(String table) {
        Assert.hasText(table, "表名不能为空");
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(table.charAt(0)));
        for (int i = 1; i < table.length(); i++) {
            sb.append('_' == table.charAt(i) ? Character.toUpperCase(table.charAt(++i)) : table.charAt(i));
        }
        return sb.toString();
    }

}
