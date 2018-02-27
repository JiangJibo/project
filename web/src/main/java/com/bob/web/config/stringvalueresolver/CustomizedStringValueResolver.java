package com.bob.web.config.stringvalueresolver;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringValueResolver;

/**
 * 通过{@link org.springframework.beans.factory.annotation.Value}实现自定义属性注入
 *
 * @author wb-jjb318191
 * @create 2018-02-27 11:32
 */
public class CustomizedStringValueResolver extends InstantiationAwareBeanPostProcessorAdapter implements StringValueResolver, BeanFactoryAware {

    /**
     * 模拟变量池
     */
    private static Map<String, String> values;

    static {
        values = new HashMap<>();
        values.put("diamond.value0", "000");
        values.put("diamond.value1", "111");
        values.put("diamond.value2", "222");
    }

    @Override
    public String resolveStringValue(String strVal) {
        String value = null;
        if (strVal.startsWith("${") && strVal.endsWith("}")) {
            String key = strVal.substring(2, strVal.length() - 1);
            if (key.startsWith("diamond")) {
                value = values.get(key);
            }
        }
        return value == null ? strVal : value;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        ((DefaultListableBeanFactory)beanFactory).addEmbeddedValueResolver(this);
    }
}
