package com.bob.common.utils.mybatis.generate.type;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;

/**
 * @author wb-jjb318191
 * @create 2019-05-07 14:38
 */
public abstract class JavaTypeResolverAdapter implements JavaTypeResolver {

    @Override
    public void addConfigurationProperties(Properties properties) {

    }

    @Override
    public void setContext(Context context) {

    }

    @Override
    public void setWarnings(List<String> warnings) {

    }

    @Override
    public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
        return null;
    }

    @Override
    public String calculateJdbcTypeName(IntrospectedColumn introspectedColumn) {
        return null;
    }
}
