package com.bob.intergrate.rocket;

import java.util.Properties;

import javax.annotation.PostConstruct;

import com.bob.intergrate.rocket.consumer.RocketConsumerConfiguration;
import com.bob.intergrate.rocket.integrate.ann.EnableRocket;
import com.bob.intergrate.rocket.producer.RocketProducerConfiguration;
import org.apache.rocketmq.client.log.ClientLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

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

    @Autowired
    private ConfigurableEnvironment environment;

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketContextConfig.class);

    static {
        //设置Rocket的日志
        ClientLogger.setLog(LOGGER);
    }

    /**
     * 将rocket-processor.properties内的配置信息配置到System中
     * 这样Consumer,Producer实例化时有些属性就有默认值
     */
    @PostConstruct
    private void initRocketContext() {
        Properties properties = (Properties)environment.getPropertySources().get("rocket-processor").getSource();
        for (String key : properties.stringPropertyNames()) {
            System.setProperty(key, properties.getProperty(key));
        }
    }

}
