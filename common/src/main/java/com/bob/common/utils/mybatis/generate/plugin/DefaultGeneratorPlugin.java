package com.bob.common.utils.mybatis.generate.plugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
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

    @Override
    public void initialized(IntrospectedTable introspectedTable) {

        removePropertyIsPrefix(introspectedTable);

        if (appendJavaModelDoSuffix) {
            appendJavaModelDOSuffix(introspectedTable);
        }

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

}
