package com.bob.integrate.dubbo;

import com.bob.integrate.dubbo.EnableDubboContext.APPLICATION;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Dubbo配置类选择器
 *
 * @author wb-jjb318191
 * @create 2018-05-04 10:41
 */
public class DubboContextConfigSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableDubboContext.class.getName()));
        APPLICATION application = attributes.getEnum("type");
        switch (application) {
            case CONSUMER:
                return new String[] {DubboContextConfig.ConsumerContextConfig.class.getName()};
            case PROVIDER:
                return new String[] {DubboContextConfig.ProviderContextConfig.class.getName()};
            default:
                throw new IllegalStateException("Dubbo配置类不正确");
        }
    }
}
