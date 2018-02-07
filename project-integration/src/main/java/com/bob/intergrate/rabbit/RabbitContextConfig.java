package com.bob.intergrate.rabbit;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * RabbitMQ配置类
 *
 * @author wb-jjb318191
 * @create 2018-02-06 17:01
 */
@EnableRabbit
@Configuration
@ComponentScan
@PropertySource("classpath:rabbit-config.properties")
public class RabbitContextConfig {

    @Value("${rabbit.username}")
    private String username;

    @Value("${rabbit.password}")
    private String password;

    @Value("${rabbit.host}")
    private String host;

    @Value("${rabbit.port}")
    private int port;

    @Value("${rabbit.exchange}")
    private String exchange;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * @return
     */
    @Bean
    public RabbitConnectionFactoryBean rabbitConnectionFactory() {
        return new RabbitConnectionFactoryBean();
    }

    /**
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory springRabbitConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(springRabbitConnectionFactory);
        rabbitTemplate.setExchange("lanboal");
        rabbitTemplate.setRoutingKey("123456");
        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory springRabbitConnectionFactory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(springRabbitConnectionFactory);
        return containerFactory;
    }

}
