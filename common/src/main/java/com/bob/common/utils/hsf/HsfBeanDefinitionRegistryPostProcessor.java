package com.bob.common.utils.hsf;

import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @author wb-jjb318191
 * @create 2018-05-24 9:36
 */
public class HsfBeanDefinitionRegistryPostProcessor
    implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private volatile boolean initialized = false;
    private static AnnotationAttributes enableHsfAnnAttr;

    private ConfigurableEnvironment environment;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] beanNames = registry.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition)registry.getBeanDefinition(beanName);
            Class<?> beanClass;
            try {
                beanClass = beanDefinition.resolveBeanClass(this.getClass().getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new BeanCreationException(String.format("名称为[%s]的Bean其Class不存在", beanName));
            }
            if (beanClass == null || !beanClass.isAnnotationPresent(HsfProvider.class)) {
                continue;
            }
            //TODO
            //BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(HSFSpringProviderBean.class);
            BeanDefinitionBuilder builder = null;
            builder.addPropertyReference("target", beanName);

            AnnotationAttributes providerAnnAttr = AnnotationUtils.getAnnotationAttributes(beanClass, beanClass.getAnnotation(HsfProvider.class));
            // 当 HsfProvider 上未指定HSF接口名称时, 默认使用Service实现的第一个接口
            String serviceInterface = providerAnnAttr.getString("serviceInterface");
            if (!StringUtils.hasText(serviceInterface)) {
                providerAnnAttr.put("serviceInterface", beanClass.getInterfaces()[0].getName());
            }

            /**
             * 将注解上的配置信息注入到HSFSpringProviderBean中,优先使用{@link HsfProvider}的信息
             * 如果HsfProvider上的信息为默认值,那么使用{@link EnableHSF}上的信息,如果都为默认值,则不注入
             */
            for (Entry<String, Object> entry : providerAnnAttr.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (!hasSimpleValue(value)) {
                    value = enableHsfAnnAttr.get(key);
                }
                if (hasSimpleValue(value)) {
                    if (value instanceof String) {
                        String propKey = (String)value;
                        if (propKey.startsWith("${") && propKey.endsWith("}")) {
                            environment.resolvePlaceholders(propKey);
                        }
                    }
                    builder.addPropertyValue(key, value);
                }
            }
            AbstractBeanDefinition hsfBeanDefinition = builder.getBeanDefinition();
            String hsfBeanName = BeanDefinitionReaderUtils.generateBeanName(hsfBeanDefinition, registry);
            registry.registerBeanDefinition(hsfBeanName, hsfBeanDefinition);
        }
        initialized = true;
    }

    /**
     * 是否含有非默认值
     *
     * @param value
     * @return
     */
    private boolean hasSimpleValue(Object value) {
        if (value instanceof String) {
            return StringUtils.hasText((String)value);
        }
        if (value instanceof Integer) {
            return ((Integer)value).intValue() > 0;
        }
        if (value instanceof Long) {
            return ((Long)value).longValue() > 0;
        }
        return false;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (!initialized) {
            postProcessBeanDefinitionRegistry((BeanDefinitionRegistry)beanFactory);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment)environment;
    }

    public static void setEnableHsfAnnAttr(AnnotationAttributes enableHsfAnnAttr) {
        HsfBeanDefinitionRegistryPostProcessor.enableHsfAnnAttr = enableHsfAnnAttr;
    }
}
