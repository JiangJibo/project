package com.bob.root.concrete.annotation;

import com.bob.root.config.aliasfor.AliasForAnnotatedClass;
import com.bob.root.config.aliasfor.AliasForAnnotations;
import org.junit.Test;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.TypeFilter;

/**
 * {@link AliasFor}注解测试
 *
 * @author Administrator
 * @create 2018-05-03 20:37
 */
public class AliasForAnnotationTest {

    @Test
    public void testAliasFor(){
        AnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(AliasForAnnotatedClass.class);
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(AliasForAnnotations.class.getName()));
        String[] packages = attributes.getStringArray("packages");
        System.out.println(packages[0]);
        Class[] packageClasses = attributes.getClassArray("packageClasses");
        System.out.println(packageClasses[0].getName());
        Class<? extends TypeFilter> clazz = attributes.getClass("filterClass");
        System.out.println(clazz.getName());
    }

}
