package com.bob.project.utils.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.bob.project.utils.validate.ann.Payload;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.util.ReflectionUtils;

/**
 * 数据校验切面
 *
 * @author wb-jjb318191
 * @create 2018-01-31 16:38
 */
@Aspect
@Order(1)
public class DataValidateAdvisor {

    /**
     * 定义Before AOP,代理所有标识了{@linkplain DataValidate}的方法
     *
     * @param joinpoint
     */
    @Before("@annotation(com.bob.project.utils.validate.DataValidate)")
    public void validateBefore(JoinPoint joinpoint) {
        Method method = ((MethodSignature)joinpoint.getSignature()).getMethod();
        Object validatedArg = joinpoint.getArgs()[method.getDeclaredAnnotation(DataValidate.class).order()];
        doValidating(validatedArg, ValidateContextHolder.getMethodMapping(method));
    }

    /**
     * 校验数据
     *
     * @param arg
     * @param elements
     */
    private void doValidating(Object arg, Set<ValidatedElement> elements) {
        if (arg == null) {
            return;
        }
        Collection<Object> objects = extractValues(arg);
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
     * @param arg
     * @return
     */
    private Collection<Object> extractValues(Object arg) {
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
