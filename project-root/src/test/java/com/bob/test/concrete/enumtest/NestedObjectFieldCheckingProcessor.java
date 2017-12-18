package com.bob.test.concrete.enumtest;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

/**
 * 嵌套对象属性验证枚举
 *
 * @author wb-jjb318191
 * @create 2017-12-18 9:20
 */
public enum NestedObjectFieldCheckingProcessor {

    MAP(Map.class) {
        @Override
        public void process(Object object, Object expectedFieldVal) {
            Map<?, ?> map = (Map)object;
            if (CollectionUtils.isEmpty(map)) {
                return;
            }
            COLLECTION.process(map.keySet(), expectedFieldVal);
            COLLECTION.process(map.values(), expectedFieldVal);
        }
    },

    COLLECTION(Collection.class) {
        @Override
        public void process(Object object, Object expectedFieldVal) {
            if (CollectionUtils.isEmpty((Collection)object)) {
                return;
            }
            for (Object element : (Collection)object) {
                doProcessingInternal(element, expectedFieldVal);
            }
        }
    },

    PLAIN_OBJECT(Object.class) {
        @Override
        public void process(Object object, Object expectedFieldVal) {
            Class<?> clazz = object.getClass();
            Field field = FIELD_MAPPINGS.get(clazz);
            if (field == null) {
                field = ReflectionUtils.findField(clazz, CAMPUS_ID);
                field.setAccessible(true);
                FIELD_MAPPINGS.put(clazz, field != null ? field : NON_CAMPUS_ID_FIELD);
            } else if (field == NON_CAMPUS_ID_FIELD) {
                return;
            }
            Object actualFieldValue = ReflectionUtils.getField(field, object);
            Assert.isTrue(expectedFieldVal.equals(actualFieldValue), clazz.getSimpleName());
        }
    };

    private Class<?> clazz;
    private static final String CAMPUS_ID = "campusId";
    private static final Map<Class<?>, Field> FIELD_MAPPINGS = new ConcurrentHashMap<Class<?>, Field>();
    private static final Field NON_CAMPUS_ID_FIELD = ReflectionUtils.findField(NestedObjectFieldCheckingProcessor.class, CAMPUS_ID);

    NestedObjectFieldCheckingProcessor(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * @param clazz
     * @return
     */
    public static NestedObjectFieldCheckingProcessor valueOf(Class<?> clazz) {
        for (NestedObjectFieldCheckingProcessor processorEnum : NestedObjectFieldCheckingProcessor.values()) {
            if (processorEnum.clazz.isAssignableFrom(clazz)) {
                return processorEnum;
            }
        }
        return null;
    }

    /**
     * @param value
     * @param expectedFieldVal
     */
    protected void doProcessingInternal(Object value, Object expectedFieldVal) {
        if (value != null) {
            valueOf(value.getClass()).process(value, expectedFieldVal);
        }
    }

    /**
     * @param object
     * @param expectedFieldVal
     */
    public abstract void process(Object object, Object expectedFieldVal);

}