/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.web.config.scanfilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bob.web.config.userenv.AppUserContextConfig;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.util.ClassUtils;

/**
 * 剔除扫描到基础配置类{@code MvcContextConfig}通过 {@code @Import}导入的配置类路径下的Class
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2017年7月31日 下午11:00:15
 */
public class MvcContextScanExcludeFilter extends AbstractClassTestingTypeFilter {

    private static List<String> excludePackages = new ArrayList<String>();

    static {
        List<Class<?>> excludeConfigClasses = Arrays.asList(AppUserContextConfig.class);
        for (Class<?> imClazz : excludeConfigClasses) {
            excludePackages.add(ClassUtils.getPackageName(imClazz));
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.core.type.filter.AbstractClassTestingTypeFilter#match(org.springframework.core.type.ClassMetadata)
     */
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
