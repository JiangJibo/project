package com.bob.project.utils.validate;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodIntrospector.MetadataLookup;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

/**
 * 数据校验后处理器
 * 验证{@linkplain DataValidate}注解内include和exclude指定的属性是否匹配
 * 缓存method的待校验属性信息
 *
 * @author wb-jjb318191
 * @create 2018-01-31 13:21
 */
public class ValidatePostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

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
     * 内省Bean的Class上是否含有{@linkplain DataValidate}标识的方法,初始化待校验的对象
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
            ValidateProcessor.eagerInitElements(paramClass, ann.group());
        }
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

}
