package com.bob.root.config.aliasfor;

import com.bob.common.utils.hsf.HsfTypeFilter;
import com.bob.root.config.RootContextConfig;

/**
 * @author Administrator
 * @create 2018-05-03 20:26
 */
@AliasForAnnotations(packages = "base", packageClasses = RootContextConfig.class, filterClass = HsfTypeFilter.class)
public class AliasForAnnotatedClass {

}
