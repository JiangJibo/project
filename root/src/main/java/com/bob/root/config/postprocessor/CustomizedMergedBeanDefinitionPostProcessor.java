package com.bob.root.config.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * 自定义MergedPostProcessor
 *
 * @author Administrator
 * @create 2018-02-23 11:20
 */
public class CustomizedMergedBeanDefinitionPostProcessor implements MergedBeanDefinitionPostProcessor {

    /**
     * 此方法一般用来操作BeanDefinition，比如添加/修改/删除{@link PropertyValue}，设置{@linkplain AbstractBeanDefinition#autowireMode}
     * 通过XML配置文件加载的Bean,其autowireMode值为{@link AbstractBeanDefinition#AUTOWIRE_BY_NAME},{@link AbstractBeanDefinition#AUTOWIRE_BY_TYPE}
     * {@linkplain AbstractBeanDefinition#autowireMode}默认值是{@link AbstractBeanDefinition#AUTOWIRE_NO},代表着BeanDefinition属性内不注入到Bean实例中
     *
     * @param beanDefinition
     * @param beanType
     * @param beanName
     */
    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.add("userName", "lanboal");
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
