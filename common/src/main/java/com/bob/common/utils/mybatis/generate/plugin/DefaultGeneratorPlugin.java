package com.bob.common.utils.mybatis.generate.plugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.springframework.util.ReflectionUtils;

import static com.bob.common.utils.mybatis.generate.constant.GeneratorContextConfig.APPEND_JAVA_MODEL_DO_SUFFIX;

/**
 * @author wb-jjb318191
 * @create 2019-07-18 15:17
 */
public class DefaultGeneratorPlugin extends PluginAdapter {

    @Override
    public void initialized(IntrospectedTable introspectedTable) {

        removePropertyIsPrefix(introspectedTable);

        if (APPEND_JAVA_MODEL_DO_SUFFIX) {
            appendJavaModelDOSuffix(introspectedTable);
        }

    }

    /**
     * 移除属性名称的is前缀
     *
     * @param introspectedTable
     */
    private void removePropertyIsPrefix(IntrospectedTable introspectedTable) {
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            String javaProperty = column.getJavaProperty();
            if (javaProperty.startsWith("is")) {
                javaProperty = (char)(javaProperty.charAt(2) + 32) + javaProperty.substring(3);
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
            Object key = entry.getKey();
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
