package com.bob.integrate.dubbo.consumer;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Administrator
 * @create 2018-05-06 19:25
 */
@EnableDubbo
@ComponentScan
@PropertySource("classpath:dubbo-consumer.properties")
public class DubboConsumerContextConfig {
}
