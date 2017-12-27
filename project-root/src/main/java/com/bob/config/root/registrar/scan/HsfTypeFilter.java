package com.bob.config.root.registrar.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.util.ClassUtils;

/**
 * 自定义HSF类型过滤，过滤抽象类,接口,注解,枚举,内部类及匿名类
 *
 * @author wb-jjb318191
 * @create 2017-12-25 10:23
 */
public class HsfTypeFilter extends AbstractClassTestingTypeFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HsfTypeFilter.class);

    @Override
    protected boolean match(ClassMetadata metadata) {
        Class<?> clazz = transformToClass(metadata.getClassName());
        if (clazz != null) {
            //过滤抽象类,接口,注解,枚举,内部类及匿名类
            return !metadata.isAbstract() && !clazz.isInterface() && !clazz.isAnnotation() && !clazz.isEnum()
                && !clazz.isMemberClass() && !clazz.getName().contains("$");
        }
        return false;
    }

    /**
     * @param className
     * @return
     */
    private Class<?> transformToClass(String className) {
        Class<?> clazz = null;
        try {
            clazz = ClassUtils.forName(className, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.info("未找到指定HSF基础类{}", className);
        }
        return clazz;
    }
}
