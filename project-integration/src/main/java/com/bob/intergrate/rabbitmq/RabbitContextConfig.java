package com.bob.intergrate.rabbitmq;

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
     * @param rabbitConnectionFactoryBean
     * @return
     * @throws Exception
     */
    @Bean
    public ConnectionFactory springRabbitConnectionFactory(RabbitConnectionFactoryBean rabbitConnectionFactoryBean) throws Exception {
        return new SpringRabbitConnectionFactory(rabbitConnectionFactoryBean.getObject());
    }

    /**
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory springRabbitConnectionFactory) {
        return new RabbitTemplate(springRabbitConnectionFactory);
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(){
        return new SimpleRabbitListenerContainerFactory();
    }

}
