package com.bob.config.root.registrar.scan;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

/**
 * HSF后处理器
 *
 * @author wb-jjb318191
 * @create 2017-12-25 10:51
 */
public class HsfBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;
    private HsfGenericConfig hsfGenericConfig;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        hsfGenericConfig = beanFactory.getBean(HsfGenericConfig.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //if (bean instanceof HSFSpringProviderBean && isScanedGeneratedHsfBean(beanName)) {
            BeanUtils.copyProperties(hsfGenericConfig, bean);
            Class<?> underlyingClass = HsfBeanDefinitionRegistrar.getUnderlyingClass(beanName);
            Object underlyingBean = beanFactory.getBean(underlyingClass);
            //((HSFSpringProviderBean)bean).setTarget(underlyingBean);
            //((HSFSpringProviderBean)bean).setServiceInterface(underlyingClass.getInterfaces()[0].getName());
        //}
        return bean;
    }

    /**
     * 当前Bean是否是通过{@linkplain HsfComponentScan}注解式生成的Bean
     *
     * @param beanName
     * @return
     */
    private boolean isScanedGeneratedHsfBean(String beanName) {
        //return beanName.startsWith(HSFSpringProviderBean.class.getSimpleName()) && beanName.contains("#");
        return false;
    }
}
