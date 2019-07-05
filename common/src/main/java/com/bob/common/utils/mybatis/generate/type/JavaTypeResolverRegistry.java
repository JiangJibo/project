package com.bob.common.utils.mybatis.generate.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import static com.bob.common.utils.mybatis.generate.constant.GeneratorContextConfig.TYPE_RESOLVER_CLASS;

/**
 * java类型解析注册器
 *
 * @author wb-jjb318191
 * @create 2019-05-07 14:16
 */
public class JavaTypeResolverRegistry extends JavaTypeResolverAdapter {

    private List<Class> typeResolverClasses = TYPE_RESOLVER_CLASS;

    private List<JavaTypeResolver> typeResolvers = new ArrayList<>(typeResolverClasses.size());

    @Override
    public void addConfigurationProperties(Properties properties) {
        initTypeResolvers();
        // 将默认的类型解析器放在最后做兜底解析
        typeResolvers.add(new JavaTypeResolverDefaultImpl());
    }

    @Override
    public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
        for (JavaTypeResolver typeResolver : typeResolvers) {
            FullyQualifiedJavaType javaType = typeResolver.calculateJavaType(introspectedColumn);
            if (javaType != null) {
                return javaType;
            }
        }
        return null;
    }

    @Override
    public String calculateJdbcTypeName(IntrospectedColumn introspectedColumn) {
        for (JavaTypeResolver typeResolver : typeResolvers) {
            String jdbcTypeName = typeResolver.calculateJdbcTypeName(introspectedColumn);
            if (jdbcTypeName != null) {
                return jdbcTypeName;
            }
        }
        return null;
    }

    private void initTypeResolvers() {
        typeResolverClasses.forEach(clazz -> {
            Assert.isAssignable(JavaTypeResolver.class, clazz, String.format("%s必须是%s的实现类", clazz.getName(), JavaTypeResolver.class.getName()));
            typeResolvers.add(BeanUtils.instantiateClass(clazz,JavaTypeResolver.class));
        });
    }

}
