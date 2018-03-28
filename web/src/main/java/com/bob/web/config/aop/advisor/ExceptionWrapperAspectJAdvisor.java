package com.bob.web.config.aop.advisor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 异常信息封装切面
 *
 * @author wb-jjb318191
 * @create 2018-03-28 10:04
 */
@Aspect
@Order(1)
public class ExceptionWrapperAspectJAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionWrapperAspectJAdvisor.class);

    private static final Map<Class<?>, Field> RESULT_INFO_FIELD_MAPPINGS = new ConcurrentHashMap<Class<?>, Field>();
    private static final Map<Class<?>, Field> RESULT_FLAG_FIELD_MAPPINGS = new ConcurrentHashMap<Class<?>, Field>();

    /**
     * 封装Controller层异常信息,包装成原始对象返回
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    @Around("com.bob.web.config.aop.pointcut.PointcutArchitecture.controllerMethod()")
    public Object wrapException(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            LOGGER.error("[{}]执行失败", method.getName(), e);
            Class returnType = ((MethodSignature)joinPoint.getSignature()).getMethod().getReturnType();
            return generateErrorResult(returnType, e.getMessage());
        }
    }

    /**
     * @param clazz
     * @param errorMsg
     * @param <T>
     * @return
     */
    private <T> T generateErrorResult(Class<T> clazz, String errorMsg) {
        T t = BeanUtils.instantiate(clazz);
        Field contentField = selectMsgField(clazz);
        ReflectionUtils.setField(contentField, t, errorMsg);
        Field flag = selectFlagField(clazz);
        ReflectionUtils.setField(flag, t, false);
        return t;
    }

    /**
     * 获取错误信息返回属性
     *
     * @param clazz
     * @return
     */
    private Field selectMsgField(Class<?> clazz) {
        Field contentField = RESULT_INFO_FIELD_MAPPINGS.get(clazz);
        if (contentField == null) {
            contentField = ReflectionUtils.findField(clazz, "errorMsg");
            if (contentField == null) {
                contentField = ReflectionUtils.findField(clazz, "content");
            }
            if (contentField == null) {
                contentField = ReflectionUtils.findField(clazz, "result");
            }
            Assert.state(contentField != null, "存储数据属性不存在,请联系后端开发人员");
            contentField.setAccessible(true);
            RESULT_INFO_FIELD_MAPPINGS.putIfAbsent(clazz, contentField);
        }
        return contentField;
    }

    /**
     * 获取错误标识属性
     *
     * @param clazz
     * @return
     */
    private Field selectFlagField(Class<?> clazz) {
        Field flag = RESULT_FLAG_FIELD_MAPPINGS.get(clazz);
        if (flag == null) {
            flag = ReflectionUtils.findField(clazz, "success");
            Assert.state(flag != null, "请求成功与否标识属性不存在,请联系后端开发人员");
            flag.setAccessible(true);
            RESULT_FLAG_FIELD_MAPPINGS.putIfAbsent(clazz, flag);
        }
        return flag;
    }

}
