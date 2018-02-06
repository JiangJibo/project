package com.bob.project.intergrate.config.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
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
        return new AbstractConnectionFactory(new com.rabbitmq.client.ConnectionFactory()) {
            @Override
            public Connection createConnection() throws AmqpException {
                return createBareConnection();
            }
        };
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

}
