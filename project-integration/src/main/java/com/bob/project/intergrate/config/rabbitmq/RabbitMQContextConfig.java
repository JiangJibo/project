package com.bob.project.intergrate.config.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * RabbitMQ配置类
 *
 * @author wb-jjb318191
 * @create 2018-02-06 17:01
 */
@EnableRabbit
@Configuration
@PropertySource("classpath:rabbit-config.properties")
public class RabbitMQContextConfig {

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${host}")
    private String host;

    /**
     * @return
     */
    @Bean
    public ConnectionFactory rabbitMQConnectionFactory() {
        return new AbstractConnectionFactory(rabbitConnectionFactory()) {
            @Override
            public Connection createConnection() throws AmqpException {
                return createBareConnection();
            }
        };
    }

    private com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory() {
        return new com.rabbitmq.client.ConnectionFactory();
    }

    /**
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange("");
        return rabbitTemplate;
    }

}
