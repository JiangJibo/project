package com.bob.project.utils.validate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 数据校验环境
 *
 * @author wb-jjb318191
 * @create 2018-01-31 13:43
 */
public class ValidateContextHolder {

    private static Map<Class<?>, Set<ValidatedElement>> VALIDATED_CLASS_MAPPINGS = new HashMap<>();
    private static Map<Method, Set<ValidatedElement>> VALIDATED_METHOD_MAPPINGS = new HashMap<>();

    /**
     * @param clazz
     * @param elements
     */
    public static void addClassMapping(Class<?> clazz, Set<ValidatedElement> elements) {
        VALIDATED_CLASS_MAPPINGS.put(clazz, elements);
    }

    /**
     * @param clazz
     * @return
     */
    public static Set<ValidatedElement> getClassMapping(Class<?> clazz) {
        return VALIDATED_CLASS_MAPPINGS.get(clazz);
    }

    /**
     * @param method
     * @param elements
     */
    public static void addMethodMapping(Method method, Set<ValidatedElement> elements) {
        VALIDATED_METHOD_MAPPINGS.put(method, elements);
    }

    /**
     * @param method
     * @return
     */
    public static Set<ValidatedElement> getMethodMapping(Method method) {
        return VALIDATED_METHOD_MAPPINGS.get(method);
    }

}
