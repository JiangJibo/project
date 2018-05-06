package com.bob.integrate.dubbo;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

import com.bob.integrate.dubbo.consumer.CityDubboConsumerService;
import com.bob.integrate.dubbo.common.service.CityDubboService;
import com.bob.integrate.dubbo.provider.CityDubboServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Dubbo配置类
 *
 * @author wb-jjb318191
 * @create 2018-05-04 10:18
 */
public class DubboContextConfig {

    @Configuration
    @PropertySource("classpath:dubbo-provider.properties")
    @EnableDubbo(scanBasePackageClasses = CityDubboServiceImpl.class)
    public static class ProviderContextConfig {

    }

    @Configuration
    @PropertySource("classpath:dubbo-consumer.properties")
    @ComponentScan(basePackageClasses = CityDubboConsumerService.class)
    @EnableDubbo(scanBasePackageClasses = CityDubboConsumerService.class)
    public static class ConsumerContextConfig {

    }

}
