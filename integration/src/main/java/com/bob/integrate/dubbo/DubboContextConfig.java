package com.bob.integrate.dubbo;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

import org.springframework.context.annotation.PropertySource;

/**
 * Dubbo配置类
 *
 * @author wb-jjb318191
 * @create 2018-05-04 10:18
 */
public class DubboContextConfig {

    @PropertySource("classpath:dubbo-provider.properties")
    @EnableDubbo(scanBasePackages = "com.bob.integrate.com.alibaba.dubbo.provider")
    public static class ProviderContextConfig {

    }

    @PropertySource("classpath:dubbo-consumer.properties")
    @EnableDubbo(scanBasePackages = "com.bob.integrate.com.alibaba.dubbo.consumer")
    public static class ConsumerContextConfig {

    }

}
