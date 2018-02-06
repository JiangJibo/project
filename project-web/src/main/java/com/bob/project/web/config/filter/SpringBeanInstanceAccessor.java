package com.bob.project.web.config.filter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Spring容器工具类,用于容器外对象获取容器内的Bean
 *
 * @author wb-jjb318191
 * @create 2017-12-12 14:19
 */
public class SpringBeanInstanceAccessor implements BeanFactoryAware {

    private static BeanFactory factory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        factory = beanFactory;
    }

    /**
     * 获取指定名称的Bean
     *
     * @param beanName
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Object getBean(String beanName, Class<T> clazz) {
        return factory.getBean(beanName, clazz);
    }

    /**
     * 获取指定类型的Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Object getBean(Class<T> clazz) {
        return factory.getBean(clazz);
    }
}
