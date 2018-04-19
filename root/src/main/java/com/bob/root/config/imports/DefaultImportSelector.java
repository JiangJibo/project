package com.bob.root.config.imports;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 多个项目环境可以使用此类来切换
 *
 * @author Administrator
 * @create 2018-04-07 22:11
 */
public class DefaultImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[0];
    }
}
