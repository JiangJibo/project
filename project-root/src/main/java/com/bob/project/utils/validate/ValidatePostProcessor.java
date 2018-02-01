package com.bob.project.utils.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bob.project.utils.validate.ann.DataValidate;
import com.bob.project.utils.validate.ann.Payload;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodIntrospector.MetadataLookup;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import static com.bob.project.utils.validate.Group.DEFAULT;

/**
 * 数据校验后处理器
 * 验证{@linkplain DataValidate}注解内include和exclude指定的属性是否匹配
 * 缓存method的待校验属性信息
 *
 * @author wb-jjb318191
 * @create 2018-01-31 13:21
 */
public class ValidatePostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private static final Object LOCK = new Object();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (AopUtils.isAopProxy(bean)) {
            beanClass = AopUtils.getTargetClass(bean);
        }
        introspectValidatedMethod(beanClass);
        return bean;
    }

    /**
     * 内省Bean的Class上是否含有{@linkplain DataValidate}标识的方法
     *
     * @param clazz
     */
    private void introspectValidatedMethod(Class<?> clazz) {
        Map<Method, DataValidate> methods = MethodIntrospector.selectMethods(clazz,
            new MetadataLookup<DataValidate>() {
                @Override
                public DataValidate inspect(Method method) {
                    return method.getDeclaredAnnotation(DataValidate.class);
                }
            });
        for (Entry<Method, DataValidate> entry : methods.entrySet()) {
            DataValidate ann = entry.getValue();
            Method method = entry.getKey();
            int order = ann.order();
            Parameter[] parameters = method.getParameters();
            Assert.state(order < parameters.length, method.toString() + "上@DataValidated的order匹配");
            Class<?> paramClass = parameters[ann.order()].getType();
            if (paramClass.isArray()) {
                paramClass = paramClass.getComponentType();
            }
            if (Collection.class.isAssignableFrom(paramClass)) {
                paramClass = getGenericType(method, order, 0);
            }
            //对于Map类型的参数,只校验其value
            if (Map.class.isAssignableFrom(paramClass)) {
                paramClass = getGenericType(method, order, 1);
            }
            ValidateContextHolder.addMethodMapping(method, extractValidatedElement(paramClass, ann));
        }
    }

    /**
     * 提取方法上{@linkplain DataValidate#group()}相应的验证条目
     *
     * @param clazz
     */
    private Set<ValidatedElement> extractValidatedElement(Class<?> clazz, DataValidate ann) {
        Set<ValidatedElement> elements = ValidateContextHolder.getClassMapping(clazz);
        if (elements == null) {
            synchronized (LOCK) {
                elements = ValidateContextHolder.getClassMapping(clazz);
                if (elements == null) {
                    elements = introspectValidatedField(clazz);
                    ValidateContextHolder.addClassMapping(clazz, elements);
                }
            }
        }
        return selectElementsByGroup(elements, ann.group());
    }

    /**
     * 内省Class是否含有表单数据校验注解
     *
     * @param clazz
     */
    private Set<ValidatedElement> introspectValidatedField(Class<?> clazz) {
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
     * @param elements
     * @param group
     * @return
     */
    private Set<ValidatedElement> selectElementsByGroup(Set<ValidatedElement> elements, Group group) {
        if (group == DEFAULT) {
            return elements;
        }
        Set<ValidatedElement> selectedElements = new HashSet<>();
        for (ValidatedElement element : elements) {
            ValidatedElement ve = new ValidatedElement(element.getField());
            for (Annotation ann : element.getAnnotations()) {
                Group annGroup = (Group)ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(ann.getClass(), "group"), ann);
                if (annGroup == DEFAULT || annGroup == group) {
                    ve.addAnnotation(ann);
                }
            }
            if (ve.isQulified()) {
                selectedElements.add(ve);
            }
        }
        return selectedElements;
    }

    /**
     * 获取泛型
     *
     * @param method
     * @param order
     * @param order
     * @return
     */
    private Class<?> getGenericType(Method method, int order, int index) {
        return ResolvableType.forMethodParameter(method, order).resolveGeneric(index);
    }

    /**
     * 获取注解的顺序,以注解上{@linkplain Validators}的顺序为标准
     *
     * @param ann
     * @return
     */
    private int getOrder(Annotation ann) {
        return ann.annotationType().getDeclaredAnnotation(Payload.class).value().ordinal();
    }

}
