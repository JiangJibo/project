package com.bob.common.utils.userenv.process;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bob.common.utils.userenv.ann.UserEnv;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * {@link @UserEnv}注解的解析器
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月7日 下午5:03:15
 */
public class UserEnvAnnotationProcessor {

    final static Logger LOGGER = LoggerFactory.getLogger(UserEnvAnnotationProcessor.class);

    private static final int CACHE_LIMIT = 256;

    private static final Object INJECTION_LOCK = new Object();

    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>();

    private final Set<Class<?>> nonAnnotatedClass = new HashSet<Class<?>>();

    /**
     * 解析Class后生成的InjectionElement缓存
     */
    @SuppressWarnings("serial")
    private volatile Map<Class<?>, Set<InjectionElement>> injectionMetadataCache =

        new LinkedHashMap<Class<?>, Set<InjectionElement>>(64, 0.75f, true) {

            @Override
            protected boolean removeEldestEntry(Entry<Class<?>, Set<InjectionElement>> eldest) {
                return size() > getCacheLimit();
            }

        };

    /**
     * 默认注入的注解为{@link @UserEnv}
     */
    public UserEnvAnnotationProcessor() {
        autowiredAnnotationTypes.add(UserEnv.class);
    }

    /**
     * 处理切面
     *
     * @param joinpoint
     */
    public void process(JoinPoint joinpoint) throws IllegalAccessException {
        Object[] args = joinpoint.getArgs();
        Signature sig = joinpoint.getSignature();
        Method method = ((MethodSignature)sig).getMethod();
        boolean debug = LOGGER.isDebugEnabled();
        if (args != null && args.length != 0) {
            long t1 = System.nanoTime(), t2 = 0;
            boolean injected = false;
            StringBuilder sb = null;
            if (debug) {
                sb = new StringBuilder(1024);
                sb.append("触发Service方法执行前注入, 方法名称: [").append(sig.toLongString()).append("], ");
                sb.append("注入参数列表: ");
            }
            Annotation[][] annotations = method.getParameterAnnotations();
            int i, j;
            for (i = 0; i < annotations.length; i++) {
                Object bean = args[i];
                if (bean == null) {
                    continue;
                }
                Annotation[] pa = annotations[i];
                if (pa.length == 0) {
                    continue;
                }
                if (UserEnv.class.isInstance(pa[0])) {
                    injected = process(bean, sb);
                    continue;
                }
                for (j = 1; j < pa.length; j++) {
                    if (UserEnv.class.isInstance(pa[j])) {
                        injected = process(bean, sb);
                        break;
                    }
                }
            }
            if (debug && injected) {
                t2 = System.nanoTime();
                sb.append("\n完成注入共计耗时: ").append((t2 - t1) / 1000000.0).append("ms");
                LOGGER.debug(sb.toString());
            }
        }
    }

    /**
     * 处理Bean,注入{@link @UserEnv}注解
     *
     * @param bean
     * @param sb
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private boolean process(Object bean, StringBuilder sb) throws IllegalArgumentException, IllegalAccessException {
        Assert.notNull(bean, "被注入对象不能为空");
        Class<?> beanClass = bean.getClass();
        if (!beanClass.isAnnotationPresent(UserEnv.class)) {
            LOGGER.warn("[{}]未标识有[{}]注解", beanClass.getSimpleName(), UserEnv.class.getSimpleName());
            return false;
        }
        if (nonAnnotatedClass.contains(beanClass)) {
            LOGGER.warn("[{}]内部不含指定的[{}]注解属性或方法", beanClass.getSimpleName(), UserEnv.class.getSimpleName());
            return false;
        }
        Set<InjectionElement> injectionElements = injectionMetadataCache.get(beanClass);
        if (injectionElements == null) {
            synchronized (INJECTION_LOCK) {
                injectionElements = injectionMetadataCache.get(beanClass);
                if (injectionElements == null) {
                    injectionElements = buildAutowiredMetadata(beanClass);
                    if (injectionElements.isEmpty()) {
                        LOGGER.warn("未找到标注[{}]注解的属性或方法", autowiredAnnotationTypes.toString());
                        nonAnnotatedClass.add(beanClass);
                        return false;
                    }
                    injectionMetadataCache.put(beanClass, injectionElements);
                }
            }
        }
        return injectByField(bean, injectionElements, sb) | injectByMethod(bean, injectionElements, sb);
    }

    /**
     * 根据属性注入
     *
     * @param bean
     * @param sb
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private boolean injectByField(Object bean, Set<InjectionElement> injectionElements, StringBuilder sb)
        throws IllegalArgumentException, IllegalAccessException {
        boolean success = false;
        for (InjectionElement injectionElement : injectionElements) {
            if (!injectionElement.isField) {
                continue;
            }
            Field field = (Field)injectionElement.member;
            ReflectionUtils.makeAccessible(field);
            String fieldName = field.getName();
            String key = injectionElement.key;
            key = StringUtils.hasText(key) ? key : fieldName;
            if (!isTypeMatch(field.getType(), key, fieldName)) {
                continue;
            }
            Object value = AppUser.getUserEnv(key);
            field.set(bean, value);
            sb.append("[" + fieldName + "]  =  [" + value + "],");
            success = true;
        }
        return success;
    }

    /**
     * 根据getter()方法注入
     *
     * @param bean
     * @param sb
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    private boolean injectByMethod(Object bean, Set<InjectionElement> injectionElements, StringBuilder sb) {
        boolean success = false;
        for (InjectionElement injectionElement : injectionElements) {
            if (injectionElement.isField) {
                continue;
            }
            Method method = (Method)injectionElement.member;
            PropertyDescriptor proDesc = BeanUtils.findPropertyForMethod(method);
            String fieldName = proDesc.getName();
            String key = injectionElement.key;
            key = StringUtils.hasText(key) ? key : fieldName;
            if (!isTypeMatch(method.getReturnType(), key, fieldName)) {
                continue;
            }
            Object value = AppUser.getUserEnv(key);
            ReflectionUtils.invokeMethod(proDesc.getWriteMethod(), bean, value);
            sb.append("[" + fieldName + "]  =  [" + value + "],");
            success = true;
        }
        return success;
    }

    /**
     * 校验待注入的成员的类型
     *
     * @param fieldType
     * @param key
     * @param fieldName
     * @return
     */
    private boolean isTypeMatch(Class<?> fieldType, String key, String fieldName) {
        if (!fieldType.isAssignableFrom(String.class) && !fieldType.isAssignableFrom(Integer.class)) {
            LOGGER.warn("环境变量[{}]注入失败,暂时只支持String和int类型的注入", fieldName);
            return false;
        }
        Object value = AppUser.getUserEnv(key);
        if (null == value) {
            LOGGER.warn("环境变量[{}]注入失败,未查询到指定名称的环境变量", fieldName);
            return false;
        }
        if (!value.getClass().isAssignableFrom(fieldType)) {
            LOGGER.warn("环境变量:[{}]的类型不匹配待注入的属性:[{}]", key.getClass().getName(), fieldName);
            return false;
        }
        return true;
    }

    /**
     * 获取类上标注了{@link @UserEnv}注解的成员信息
     *
     * @param clazz
     * @return
     */
    private Set<InjectionElement> buildAutowiredMetadata(final Class<?> clazz) {

        final Set<InjectionElement> injectionElements = new HashSet<InjectionElement>();

        ReflectionUtils.doWithLocalFields(clazz, field -> {
            AnnotationAttributes ann = findAutowiredAnnotation(field);
            if (ann != null) {
                if (Modifier.isStatic(field.getModifiers())) {
                    LOGGER.warn("[{}]注解不适用于静态方法:[{}]", autowiredAnnotationTypes.toString(), field);
                    return;
                }
                injectionElements.add(new InjectionElement(field, ann.getString("value")));
            }
        });

        ReflectionUtils.doWithLocalMethods(clazz, method -> {

            AnnotationAttributes ann = findAutowiredAnnotation(method);
            if (ann != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
                if (Modifier.isStatic(method.getModifiers())) {
                    LOGGER.warn("注解不适用于静态方法: [{}]", autowiredAnnotationTypes.toString(), method);
                    return;
                }
                if (!method.getName().startsWith("get") || method.getParameterTypes().length > 0) {
                    LOGGER.warn("[{}]注解只适用于getter方法,而非: [{}]方法", autowiredAnnotationTypes.toString(), method);
                    return;
                }
                injectionElements.add(new InjectionElement(method, ann.getString("value")));
            }
        });

        return injectionElements;
    }

    /**
     * 获取一个成员成标注了{@link @UserEnv}注解(或元注解)的注解数据
     *
     * @param ao
     * @return
     */
    private AnnotationAttributes findAutowiredAnnotation(AnnotatedElement ao) {
        if (ao.getAnnotations().length > 0) {
            for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
                AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
                if (attributes != null) {
                    return attributes;
                }
            }
        }
        return null;
    }

    /**
     * 获取缓存容量
     *
     * @return
     */
    public int getCacheLimit() {
        return CACHE_LIMIT;
    }

    /**
     * 清空缓存
     */
    public void clearCache() {
        this.injectionMetadataCache.clear();
    }

    /**
     * 待注入的成员信息
     *
     * @author JiangJibo
     * @version $Id$
     * @since 2017年1月22日 上午9:12:19
     */
    private static class InjectionElement {

        private Member member;
        private boolean isField;
        private String key;

        /**
         * @param member
         * @param key
         */
        public InjectionElement(Member member, String key) {
            this.member = member;
            this.isField = (member instanceof Field);
            this.key = key;
        }

    }

}
