package com.bob.web.utils.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bob.web.utils.validate.constraints.Payload;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 数据校验工具类
 *
 * @author wb-jjb318191
 * @create 2018-02-01 14:23
 */
public class ValidateProcessor {

    private static final Object LOCK = new Object();

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
        if (elements == null) {
            elements = eagerInitElements(arg.getClass(), group);
        }
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
     * 初始化数据校验条目
     *
     * @param clazz
     * @param group
     */
    static Set<ValidatedElement> eagerInitElements(Class<?> clazz, Group group) {
        Set<ValidatedElement> elements = ValidateContextHolder.getClassMapping(clazz);
        if (elements == null) {
            synchronized (LOCK) {
                elements = ValidateContextHolder.getClassMapping(clazz);
                if (elements == null) {
                    elements = introspectAllElements(clazz);
                    ValidateContextHolder.addClassMapping(clazz, elements);
                }
                elements = ValidateContextHolder.getGroupMapping(clazz, group);
                if (elements == null) {
                    elements = introspectGroupElements(ValidateContextHolder.getClassMapping(clazz), group);
                    ValidateContextHolder.addGroupMapping(clazz, group, elements);
                }
            }
        }
        return elements;
    }

    /**
     * 内省Class是否含有表单数据校验注解
     *
     * @param clazz
     */
    private static Set<ValidatedElement> introspectAllElements(Class<?> clazz) {
        Set<ValidatedElement> elements = new HashSet<>();
        ReflectionUtils.doWithFields(clazz,
            (field -> {
                field.setAccessible(true);
                List<Annotation> annotations = new ArrayList<>();
                for (Annotation ann : field.getDeclaredAnnotations()) {
                    if (ann.annotationType().isAnnotationPresent(Payload.class)) {
                        annotations.add(ann);
                    }
                }
                Collections.sort(annotations, Comparator.comparingInt((ann) -> getOrder((Annotation)ann)));
                elements.add(new ValidatedElement(field, annotations));
            }),
            (field -> {
                for (Annotation ann : field.getDeclaredAnnotations()) {
                    if (ann.annotationType().isAnnotationPresent(Payload.class)) {
                        return true;
                    }
                }
                return false;
            })
        );
        Assert.notEmpty(elements, "[" + clazz.getName() + "]类的属性不含有数据校验注解");
        return elements;
    }

    /**
     * 查询兼容当前分组的验证条目
     * {@linkplain Group#DEFAULT}兼容所有
     *
     * @param allElements
     * @param group
     * @return
     */
    private static Set<ValidatedElement> introspectGroupElements(Set<ValidatedElement> allElements, Group group) {
        if (group == Group.DEFAULT) {
            return allElements;
        }
        Set<ValidatedElement> groupElements = new HashSet<>();
        for (ValidatedElement element : allElements) {
            ValidatedElement ve = new ValidatedElement(element.getField());
            for (Annotation ann : element.getAnnotations()) {
                Group annGroup = (Group)ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(ann.getClass(), "group"), ann);
                if (annGroup == Group.DEFAULT || annGroup == group) {
                    ve.addAnnotation(ann);
                }
            }
            if (ve.isQualified()) {
                groupElements.add(ve);
            }
        }
        return groupElements;
    }

    /**
     * 获取注解的顺序,以注解上{@linkplain Validators}的顺序为标准
     *
     * @param ann
     * @return
     */
    private static int getOrder(Annotation ann) {
        return ann.annotationType().getDeclaredAnnotation(Payload.class).value().ordinal();
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
