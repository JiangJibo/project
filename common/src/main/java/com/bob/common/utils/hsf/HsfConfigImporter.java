package com.bob.common.utils.hsf;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wb-jjb318191
 * @create 2018-05-24 11:12
 */
public class HsfConfigImporter implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        HsfBeanDefinitionRegistryPostProcessor.setEnableHsfAnnAttr(
            AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableHSF.class.getName())));
    }
}
