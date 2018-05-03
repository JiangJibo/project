package com.bob.intergrate.dubbo.server;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

import org.springframework.context.annotation.PropertySource;

/**
 * @author wb-jjb318191
 * @create 2018-05-03 16:52
 */
@EnableDubbo
@PropertySource("classpath:dubbo-server.properties")
public class DubboServerContextConfig {
}
