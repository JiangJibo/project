package com.bob.common.utils.request.post;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.bob.common.utils.request.post.entity.CommonRiskData;
import com.bob.common.utils.request.post.entity.OORiskDataParam;
import com.bob.common.utils.request.post.entity.RiskDataParam;
import com.bob.common.utils.request.post.entity.SeedType;
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
 * @create 2019-07-18 17:25
 */
@Slf4j
public class HttpBodyAssembler implements HandlerMethodArgumentResolver {

    private static final Map<Class<?>, Map<String, Field>> CLASS_PARAMS_MAPPINGS = new ConcurrentHashMap<>();

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

        Class<? extends CommonRiskData> clazz = mappingToRiskData(dataParam.getSeedType());

        Object result = BeanUtils.instantiate(clazz);
        BeanWrapper beanWrapper = new BeanWrapperImpl(result);

        Map<String, Field> classParams = CLASS_PARAMS_MAPPINGS.get(clazz);
        if (classParams == null) {
            classParams = initClassParamFields(clazz);
            CLASS_PARAMS_MAPPINGS.putIfAbsent(clazz, classParams);
        }

        Map<String, Field> finalClassParams = classParams;
        data.forEach((key, value) -> {
            Field field = finalClassParams.get(key);
            if (field == null) {
                throw new IllegalArgumentException(String.format("param [%s] can not be resolved", key));
            }
            // 如果是数组或者集合,略过,手动处理
            if (Collection.class.isAssignableFrom(field.getType()) || field.getType().isArray()) {
                return;
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
    private Map<String, Field> initClassParamFields(Class<?> clazz) {
        Map<String, Field> params = new HashMap<>();
        ReflectionUtils.doWithFields(clazz, field -> {
            if (field.isAnnotationPresent(BodyFiled.class)) {
                params.put(field.getAnnotation(BodyFiled.class).value(), field);
            } else {
                params.put(field.getName(), field);
            }
        });
        return params;
    }

    private Class<? extends CommonRiskData> mappingToRiskData(String seedType) {
        SeedType type = SeedType.valueOf(seedType);
        switch (type) {
            case OO:
                return OORiskDataParam.class;
            default:
                throw new IllegalArgumentException(String.format("非允许的种子类型[%s]", seedType));
        }
    }

}
