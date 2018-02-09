package com.bob.web.utils.validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 校验上下文信息缓存
 *
 * @author wb-jjb318191
 * @create 2018-01-31 13:43
 */
class ValidateContextHolder {

    private static Map<Class<?>, Set<ValidatedElement>> VALIDATED_CLASS_MAPPINGS = new HashMap<>();
    private static Map<String, Set<ValidatedElement>> VALIDATED_GROUP_MAPPINGS = new HashMap<>();

    /**
     * @param clazz
     * @param elements
     */
    static void addClassMapping(Class<?> clazz, Set<ValidatedElement> elements) {
        VALIDATED_CLASS_MAPPINGS.put(clazz, elements);
    }

    /**
     * @param clazz
     * @return
     */
    static Set<ValidatedElement> getClassMapping(Class<?> clazz) {
        return VALIDATED_CLASS_MAPPINGS.get(clazz);
    }

    /**
     * @param clazz
     * @param elements
     * @param group
     */
    static void addGroupMapping(Class<?> clazz, Group group, Set<ValidatedElement> elements) {
        VALIDATED_GROUP_MAPPINGS.put(generateGroupKey(clazz, group), elements);
    }

    /**
     * @param clazz
     * @param group
     * @return
     */
    static Set<ValidatedElement> getGroupMapping(Class<?> clazz, Group group) {
        return group == Group.DEFAULT ? getClassMapping(clazz) : VALIDATED_GROUP_MAPPINGS.get(generateGroupKey(clazz, group));
    }

    /**
     * @param clazz
     * @param group
     * @return
     */
    private static String generateGroupKey(Class<?> clazz, Group group) {
        return clazz.getName() + "_" + Group.class.getSimpleName() + "_" + group.toString();
    }

}
