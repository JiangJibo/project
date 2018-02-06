package com.bob.project.intergrate.config.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 *
 * @author wb-jjb318191
 * @create 2018-02-06 17:01
 */
@EnableRabbit
@Configuration
public class RabbitMQContextConfig {

    @Bean
    public ConnectionFactory rabbitMQConnectionFactory() {
        return null;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

}
