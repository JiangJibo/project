package com.bob.common.utils.request.post.entity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 情报数据封装
 *
 * @author wb-jjb318191
 */
@Slf4j
public class HttpBodyAssembler implements HandlerMethodArgumentResolver {

    private static final Map<Class<?>, Map<String, Field>> CLASS_FIELDS_MAPPINGS = new ConcurrentHashMap<>();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(HttpBody.class) != null
            && parameter.getParameterType() == RiskDataParam.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws IOException {

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();

        String json = new String(StreamUtils.copyToByteArray(request.getInputStream()), "utf-8");

        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject data = (JSONObject)jsonObject.remove("data");

        RiskDataParam dataParam = JSON.parseObject(jsonObject.toJSONString(), RiskDataParam.class);

        Class<? extends CommonRiskData> clazz = dataParam.resolveDataType();

        Object result = BeanUtils.instantiate(clazz);
        BeanWrapper beanWrapper = new BeanWrapperImpl(result);

        Map<String, Field> classParams = CLASS_FIELDS_MAPPINGS.get(clazz);
        if (classParams == null) {
            classParams = initFieldMappings(clazz);
            CLASS_FIELDS_MAPPINGS.putIfAbsent(clazz, classParams);
        }

        Map<String, Field> finalClassParams = classParams;
        // 将data里的数据填充到对应的情报实体里
        // TODO 集合类型待处理
        data.forEach((key, value) -> {
            Field field = finalClassParams.get(key);
            if (field == null) {
                throw new IllegalArgumentException(String.format("param [%s] can not be resolved", key));
            }
            beanWrapper.setPropertyValue(field.getName(), value);
        });
        return result;
    }

    /**
     * 初始化每个Class的参数名称和Field的映射
     *
     * @param clazz
     * @return
     */
    private Map<String, Field> initFieldMappings(Class<?> clazz) {
        Map<String, Field> params = new HashMap<>();
        // 优先处理子类的Field上的注解,优先级最高
        extractFieldMapping(clazz, params);
        // 处理getter方法上的注解,优先级次高
        ReflectionUtils.doWithLocalMethods(clazz, method -> {
                if (isGetter(method) && method.isAnnotationPresent(FieldMapping.class)) {
                    params.putIfAbsent(method.getAnnotation(FieldMapping.class).value(), extractField(method));
                }
            }
        );
        // 处理父类的Field上的注解,优先级最低
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            extractFieldMapping(superClass, params);
        }
        return params;
    }

    /**
     * 获取Filed上的{@link FieldMapping}注解和其对应的Field
     *
     * @param clazz
     * @param params
     */
    private void extractFieldMapping(Class<?> clazz, Map<String, Field> params) {
        ReflectionUtils.doWithLocalFields(clazz, field -> {
            if(params.containsValue(field)){
                return;
            }
            if (field.isAnnotationPresent(FieldMapping.class)) {
                params.put(field.getAnnotation(FieldMapping.class).value(), field);
            }else{
                params.put(field.getName(), field);
            }
        });
    }

    /**
     * 判断一个函数是否是getter()方法
     *
     * @param method
     * @return
     */
    private boolean isGetter(Method method) {
        String methodName = method.getName();
        boolean getter = methodName.startsWith("get") && Character.isUpperCase(methodName.charAt(3));
        boolean isser = methodName.startsWith("is") && Character.isUpperCase(methodName.charAt(2));
        return (getter || isser) && method.getParameterCount() == 0;
    }

    /**
     * 通过getter方法获取Field
     *
     * @param method
     * @return
     */
    private Field extractField(Method method) {
        String fieldName;
        String methodName = method.getName();
        if (methodName.startsWith("get")) {
            fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        } else {
            fieldName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
        }
        return ReflectionUtils.findField(method.getDeclaringClass(), fieldName);
    }

}
