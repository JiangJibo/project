package com.bob.intergrate.config.mysql.tx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 事务配置类
 *
 * @author wb-jjb318191
 * @create 2017-12-27 9:50
 */
@Configuration
public class TransactionContextConfig {

    @Bean
    public TransactionAspectInvoker transactionAspectInvoker() {
        return new TransactionAspectInvoker();
    }

}
