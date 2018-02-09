package com.bob.intergrate.rabbit;

import com.bob.intergrate.rabbit.producer.RabbitProducer;
import com.bob.intergrate.rabbit.consumer.RabbitConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

/**
 * RabbitMQ配置类
 *
 * @author wb-jjb318191
 * @create 2018-02-06 17:01
 */
@EnableRabbit
@Configuration
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

    @Value("${rabbit.queue.default}")
    private String defaultQueue;

    @Value("${rabbit.queue.default.key}")
    private String defaultKey;

    @Value("${rabbit.queue.service}")
    private String serviceQueue;

    @Value("${rabbit.queue.service.key}")
    private String serviceKey;

    @Bean
    public RabbitProducer rabbitProducer() {
        return new RabbitProducer();
    }

    @Bean
    public RabbitConsumer rabbitConsumer() {
        return new RabbitConsumer();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setHost(host);
        factory.setPort(port);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue defaultQueue(RabbitAdmin rabbitAdmin) {
        Queue queue = new Queue(defaultQueue, true, false, false);
        queue.setAdminsThatShouldDeclare(rabbitAdmin);
        return queue;
    }

    @Bean
    public Queue serviceQueue(RabbitAdmin rabbitAdmin) {
        Queue queue = new Queue(serviceQueue, true, false, false);
        queue.setAdminsThatShouldDeclare(rabbitAdmin);
        return queue;
    }

    @Bean
    public Exchange directExchange(RabbitAdmin rabbitAdmin) {
        DirectExchange directExchange = new DirectExchange(exchange, true, false);
        directExchange.setAdminsThatShouldDeclare(rabbitAdmin);
        return directExchange;
    }

    @Bean
    public Binding defaultBinding(Exchange directExchange, Queue defaultQueue) {
        return new Binding(defaultQueue.getName(), QUEUE, directExchange.getName(), defaultKey, null);
    }

    @Bean
    public Binding serviceBinding(Exchange directExchange, Queue serviceQueue) {
        return new Binding(serviceQueue.getName(), QUEUE, directExchange.getName(), serviceKey, null);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Exchange directExchange) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(directExchange.getName());
        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        return containerFactory;
    }

}
