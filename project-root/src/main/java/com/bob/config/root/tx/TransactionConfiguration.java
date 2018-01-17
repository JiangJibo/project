package com.bob.config.root.tx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;

/**
 * 事务配置类
 *
 * @author wb-jjb318191
 * @create 2017-12-27 9:50
 */
@Configuration
public class TransactionConfiguration {

    @Bean
    public TransactionAspectInvoker transactionAspectInvoker() {
        return new TransactionAspectInvoker();
    }

}
