package com.bob.test.concrete.enumtest;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

/**
 * 方法返回值为包装对象的处理器
 *
 * @author Administrator
 * @create 2017-12-15 22:43
 */
public enum ReturningWrapProcessorEnum {

    COLLECTION(Collection.class) {
        @Override
        public void process(Object object, Object originalFieldValue) {
            if (CollectionUtils.isEmpty((Collection)object)) {
                return;
            }
            for (Object obj : (Collection)object) {
                ReturningWrapProcessorEnum processor = valueOf(obj.getClass());
                if (processor != null) {
                    processor.process(obj, originalFieldValue);
                } else {
                    doFieldCkecking(obj, originalFieldValue);
                }
            }
        }
    },

    MAP(Map.class) {
        @Override
        public void process(Object object, Object originalFieldValue) {
            Map<?, ?> map = (Map)object;
            if (CollectionUtils.isEmpty(map)) {
                return;
            }
            COLLECTION.process(map.keySet(), originalFieldValue);
            COLLECTION.process(map.values(), originalFieldValue);
        }

    };;

    private Class<?> clazz;
    private static final String CAMPUS_ID = "campusId";
    private static final Field NON_CAMPUS_ID_FIELD = ReflectionUtils.findField(ReturningWrapProcessorEnum.class, CAMPUS_ID);
    private static final Map<Class<?>, Field> CLASS_TO_FIELD_MAPPING = new ConcurrentHashMap<Class<?>, Field>();

    ReturningWrapProcessorEnum(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * @param clazz
     * @return
     */
    public ReturningWrapProcessorEnum valueOf(Class<?> clazz) {
        for (ReturningWrapProcessorEnum processorEnum : ReturningWrapProcessorEnum.values()) {
            if (processorEnum.clazz.isAssignableFrom(clazz)) {
                return processorEnum;
            }
        }
        return null;
    }

    /**
     * @param object
     * @return
     */
    protected Object getNestedObject(Object object) {
        return null;
    }

    /**
     * @param value
     * @param originalValue
     */
    protected void doFieldCkecking(Object value, Object originalValue) {
        Class<?> clazz = value.getClass();
        Field field = CLASS_TO_FIELD_MAPPING.get(clazz);
        if (field == null) {
            field = ReflectionUtils.findField(clazz, CAMPUS_ID);
            CLASS_TO_FIELD_MAPPING.put(clazz, field != null ? field : NON_CAMPUS_ID_FIELD);
        } else if (field == NON_CAMPUS_ID_FIELD) {
            return;
        } else {
            Object fieldValue = ReflectionUtils.getField(field, value);
            Assert.isTrue(fieldValue.equals(originalValue), clazz.getSimpleName());
        }
    }

    /**
     * @param object
     * @param originalFieldValue
     */
    public abstract void process(Object object, Object originalFieldValue);

}