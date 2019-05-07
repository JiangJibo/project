package com.bob.common.utils.mybatis.generate.type.resolver;

import java.sql.JDBCType;

import com.bob.common.utils.mybatis.generate.type.JavaTypeResolverAdapter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * JdbcType.Tinyint到Integer的转换
 *
 * @author wb-jjb318191
 * @create 2019-05-07 14:18
 */
public class TinyintToIntegerResolver extends JavaTypeResolverAdapter {

    @Override
    public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.getJdbcType() == JDBCType.TINYINT.getVendorTypeNumber()) {
            return new FullyQualifiedJavaType(Integer.class.getName());
        }
        return null;
    }

}
