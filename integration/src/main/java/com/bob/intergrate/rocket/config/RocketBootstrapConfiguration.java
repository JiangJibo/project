package com.bob.intergrate.rocket.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * Rocket整合配置类
 *
 * @author wb-jjb318191
 * @create 2018-03-20 9:24
 */
@Configuration
public class RocketBootstrapConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RocketListenerAnnotationPostProcessor rocketListenerAnnotationProcessor() {
        return new RocketListenerAnnotationPostProcessor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RocketListenerAnnotationBeanPostProcessor rocketListenerAnnotationBeanPostProcessor() {
        return new RocketListenerAnnotationBeanPostProcessor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RocketConsumerStarter defaultRocketListenerEndpointRegistry() {
        return new RocketConsumerStarter();
    }

}
