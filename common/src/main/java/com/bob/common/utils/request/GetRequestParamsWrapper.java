package com.bob.common.utils.request;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Get请求param参数封装
 *
 * @author wb-jjb318191
 * @create 2019-07-18 17:25
 */
@Slf4j
public class GetRequestParamsWrapper implements HandlerMethodArgumentResolver {

    private static final Map<Class<?>, Map<String, Field>> CLASS_PARAMS_MAPPINGS = new ConcurrentHashMap<>();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(GetRequestParams.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        Class<?> parameterClass = parameter.getParameterType();

        Object result = BeanUtils.instantiate(parameterClass);
        BeanWrapper beanWrapper = new BeanWrapperImpl(result);

        Map<String, Field> classParams = CLASS_PARAMS_MAPPINGS.get(parameterClass);
        if (classParams == null) {
            classParams = initClassParamFields(parameterClass);
            CLASS_PARAMS_MAPPINGS.putIfAbsent(parameterClass, classParams);
        }

        Map<String, String[]> params = request.getParameterMap();

        for (Entry<String, String[]> param : params.entrySet()) {
            String key = param.getKey();
            Field field = classParams.get(key);
            if (field == null) {
                throw new IllegalArgumentException(String.format("param [%s] can not be resolved", key));
            }
            // 如果是数组或者集合,略过,手动处理
            if (Collection.class.isAssignableFrom(field.getType()) || field.getType().isArray()) {
                continue;
            }
            beanWrapper.setPropertyValue(field.getName(), param.getValue()[0]);
        }
        return result;
    }

    /**
     * 初始化每个Class的参数名称和Field的映射
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> initClassParamFields(Class<?> clazz) {
        Map<String, Field> params = new HashMap<>();
        ReflectionUtils.doWithLocalFields(clazz, field -> {
            if (field.isAnnotationPresent(GetParam.class)) {
                params.put(field.getAnnotation(GetParam.class).value(), field);
            } else {
                params.put(field.getName(), field);
            }
        });
        return params;
    }

}
