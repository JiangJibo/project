package com.bob.intergrate.rocket;

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
    public RocketListenerAnnotationBeanPostProcessor rocketListenerAnnotationProcessor(){
        return new RocketListenerAnnotationBeanPostProcessor();
    }

}
