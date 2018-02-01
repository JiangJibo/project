package com.bob.project.utils.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.bob.project.utils.validate.constraint.Payload;
import org.springframework.util.ReflectionUtils;

/**
 * 数据校验工具类
 *
 * @author wb-jjb318191
 * @create 2018-02-01 14:23
 */
public class ValidateProcessor {

    /**
     * 校验数据
     *
     * @param arg
     * @param group
     */
    public static void doValidating(Object arg, Group group) {
        if (arg == null) {
            return;
        }
        Collection<Object> objects = wrapValues(arg);
        Set<ValidatedElement> elements = ValidateContextHolder.getGroupMapping(arg.getClass(), group);
        for (Object obj : objects) {
            for (ValidatedElement element : elements) {
                Field field = element.getField();
                Object value = ReflectionUtils.getField(field, obj);
                for (Annotation ann : element.getAnnotations()) {
                    ann.annotationType().getDeclaredAnnotation(Payload.class).value().validate(field, value, ann);
                }
            }
        }
    }

    /**
     * 将待校验对象封装成集合对象
     *
     * @param arg
     * @return
     */
    private static Collection<Object> wrapValues(Object arg) {
        if (arg.getClass().isArray()) {
            return Arrays.asList((Object[])arg);
        }
        if (Collection.class.isAssignableFrom(arg.getClass())) {
            return (Collection<Object>)arg;
        }
        if (Map.class.isAssignableFrom(arg.getClass())) {
            return ((Map)arg).values();
        }
        return Arrays.asList(arg);
    }

}
