package com.bob.intergrate.rocket;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.log.ClientLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * Rocket消息队列配置
 *
 * @author wb-jjb318191
 * @create 2018-02-11 15:09
 */
@Configuration
@PropertySource(name = "rocket-config", value = "classpath:rocket-config.properties")
public class RocketContextConfig {

    @Autowired
    private ConfigurableEnvironment environment;

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketContextConfig.class);

    static {
        //设置Rocket的日志
        ClientLogger.setLog(LOGGER);
    }

    /**
     * 将Rocket-config.properties内的配置信息配置到环境中
     */
    @PostConstruct
    private void initRocketContext() {
        Properties properties = (Properties)environment.getPropertySources().get("rocket-config").getSource();
        for (String key : properties.stringPropertyNames()) {
            System.setProperty(key, properties.getProperty(key));
        }
    }

    @Bean
    public RocketProducer rocketProducer() {
        return new RocketProducer();
    }

    @Bean
    public RocketConsumer rocketConsumer() {
        return new RocketConsumer();
    }

}
