package com.bob.integrate.dubbo.provider;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.rpc.Filter;

import com.bob.integrate.dubbo.common.extension.LoginCheckingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Administrator
 * @create 2018-05-06 19:24
 */
@EnableDubbo
@Configuration
@PropertySource("classpath:dubbo-provider.properties")
public class DubboProviderContextConfig {

    static {
        ExtensionLoader.getExtensionLoader(Filter.class).addExtension("loginCheck", LoginCheckingFilter.class);
    }

}
