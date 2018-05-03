package com.bob.intergrate.dubbo.client;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

import org.springframework.context.annotation.PropertySource;

/**
 * @author wb-jjb318191
 * @create 2018-05-03 16:54
 */
@EnableDubbo
@PropertySource("classpath:dubbo-client.properties")
public class DubboClientContextConfig {
}
