/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 */
package com.bob.project.config.scanfilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * {@linkplain Component}扫描到的类是否匹配作为一个容器的Bean
 * @since 2017年1月13日 上午9:31:37
 * @version $Id$
 * @author JiangJibo
 *
 */
public class RootContextScanExcludeFilter extends AbstractClassTestingTypeFilter {

    final static Logger LOGGER = LoggerFactory.getLogger(RootContextScanExcludeFilter.class);

    private static List<String> excludePackages = new ArrayList<String>();

    static {
        List<Class<?>> excludeConfigClasses = Arrays.asList();
        for (Class<?> imClazz : excludeConfigClasses) {
            excludePackages.add(ClassUtils.getPackageName(imClazz));
        }
    }

    @Override
    protected boolean match(ClassMetadata metadata) {
        String packageName = ClassUtils.getPackageName(metadata.getClassName());
        for (String pn : excludePackages) {
            if (packageName.contains(pn)) {
                return true;
            }
        }
        return false;
    }

}
