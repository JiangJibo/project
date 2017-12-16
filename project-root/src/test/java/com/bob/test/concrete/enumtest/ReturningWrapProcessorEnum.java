package com.bob.test.concrete.enumtest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

/**
 * 方法返回值为包装对象的处理器
 *
 * @author Administrator
 * @create 2017-12-15 22:43
 */
public enum ReturningWrapProcessorEnum {

    lIST("LIST", List.class) {
        @Override
        public void process(Object object) {

        }

        @Override
        protected boolean doFieldCkecking(Object value, String fieldName, Object originalValue) {
            for (Object obj : (List)value) {
                ReturningWrapProcessorEnum processor = valueOf(obj);
                if (processor != null) {
                    processor.process(obj);
                }
            }
            return false;
        }

        @Override
        protected Object getNestedObject(Object object) {
            List<?> list = (List<?>)object;
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            return list.get(0);
        }
    },
    COLLECTION("COLLECTION", Collection.class) {
        @Override
        protected boolean doFieldCkecking(Object value, String fieldName, Object originalValue) {
            return false;
        }

        @Override
        public void process(Object object) {

        }

        @Override
        protected Object getNestedObject(Object object) {
            Collection c = (Collection)object;
            if (CollectionUtils.isEmpty(c)) {
                return null;
            }
            return ((Collection)object).iterator().next();
        }
    },
    MAP("MAP", Map.class) {
        @Override
        protected Object getNestedObject(Object object) {
            Map<?, ?> map = (Map<?, ?>)object;
            if (CollectionUtils.isEmpty(map)) {
                return null;
            }
            return map.values().iterator().next();
        }

        @Override
        public void process(Object object) {

        }
    };;

    private String label;
    private Class<?> clazz;

    ReturningWrapProcessorEnum(String label, Class<?> clazz) {
        this.label = label;
        this.clazz = clazz;
    }

    public ReturningWrapProcessorEnum valueOf(Object object) {
        for (ReturningWrapProcessorEnum processorEnum : ReturningWrapProcessorEnum.values()) {
            if (processorEnum.clazz.isAssignableFrom(object.getClass())) {
                return processorEnum;
            }
        }
        return null;
    }

    protected Object getNestedObject(Object object) {
        return null;
    }

    protected boolean doFieldCkecking(Object value, String fieldName, Object originalValue) {
        return false;
    }

    public abstract void process(Object object);

    public String getLabel() {
        return label;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}