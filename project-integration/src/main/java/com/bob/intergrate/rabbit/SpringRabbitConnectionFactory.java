package com.bob.intergrate.rabbit;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;

/**
 * 自定义Rabbit连接工厂
 *
 * @author wb-jjb318191
 * @create 2018-02-07 17:37
 */
public class SpringRabbitConnectionFactory extends AbstractConnectionFactory {

    /**
     * Create a new AbstractConnectionFactory for the given target ConnectionFactory,
     * with no publisher connection factory.
     *
     * @param rabbitConnectionFactory the target ConnectionFactory
     */
    public SpringRabbitConnectionFactory(ConnectionFactory rabbitConnectionFactory) {
        super(rabbitConnectionFactory);
    }

    @Override
    public Connection createConnection() throws AmqpException {
        return super.createBareConnection();
    }
}
