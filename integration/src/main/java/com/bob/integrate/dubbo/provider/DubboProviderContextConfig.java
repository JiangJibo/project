package com.bob.integrate.dubbo.provider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Administrator
 * @create 2018-05-06 19:24
 */
@EnableDubbo
@PropertySource("classpath:dubbo-provider.properties")
public class DubboProviderContextConfig {
}
