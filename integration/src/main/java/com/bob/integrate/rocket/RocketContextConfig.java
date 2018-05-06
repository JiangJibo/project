package com.bob.integrate.rocket;

import com.bob.common.utils.rocket.ann.EnableRocket;
import com.bob.integrate.rocket.consumer.RocketConsumerConfiguration;
import com.bob.integrate.rocket.producer.RocketProducerConfiguration;
import org.apache.rocketmq.client.log.ClientLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Rocket消息队列配置
 *
 * @author wb-jjb318191
 * @create 2018-02-11 15:09
 */
@EnableRocket
@Configuration
@PropertySource(name = "rocket-processor", value = "classpath:rocket-config.properties")
@Import({RocketConsumerConfiguration.class, RocketProducerConfiguration.class})
public class RocketContextConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketContextConfig.class);

    static {
        //设置Rocket的日志
        ClientLogger.setLog(LOGGER);
    }

}
