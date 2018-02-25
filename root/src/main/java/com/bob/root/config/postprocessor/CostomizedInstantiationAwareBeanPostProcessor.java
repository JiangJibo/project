package com.bob.root.config.postprocessor;

import java.beans.PropertyDescriptor;

import com.bob.root.utils.model.RootUser;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * 自定义实例化处理器
 *
 * @author Administrator
 * @create 2018-02-23 15:50
 */
public class CostomizedInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    /**
     * 此方法应该对{@link PropertyValues}进行筛选过滤操作，可自定义自己的过滤逻辑
     * (增加键值对应该放在{@link MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition(RootBeanDefinition, Class, String)}内做会好些)
     * 这样就能间接的对Bean内的属性进行赋值或者过滤
     *
     * @param pvs
     * @param pds
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        MutablePropertyValues mpv = (MutablePropertyValues)pvs;
        if (bean instanceof RootUser) {
            mpv.add("userName", "lanboal");
            mpv.removePropertyValue("password");
        }
        return mpv;
    }
}
