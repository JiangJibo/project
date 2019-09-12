package com.bob.root.config.imports;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Administrator
 * @create 2018-04-07 22:12
 */
@Configuration
public class DefaultImportAware implements ImportAware {

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
    }
}
