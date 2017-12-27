package com.bob.config.root.tx;

import java.sql.Connection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;

/**
 * 事务通知
 *
 * @author wb-jjb318191
 * @create 2017-12-27 9:50
 */
@Configuration
public class TransactionConfiguration {

    @Bean
    public TransactionAspectInvoker transactionAspectInvoker() {
        TransactionAspectInvoker txInvoker = new TransactionAspectInvoker();
        //设置事务的属性
        DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
        transactionAttribute.setIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ);
        transactionAttribute.setTimeout(3000);
        //设置事务源
        MatchAlwaysTransactionAttributeSource txAttrBs = new MatchAlwaysTransactionAttributeSource();
        txAttrBs.setTransactionAttribute(transactionAttribute);
        txInvoker.setTransactionAttributeSource(txAttrBs);
        //可以配置多个事务属性源,对不同的方法生成不同的事务属性
        //txInvoker.setTransactionAttributeSources(new TransactionAttributeSource[] {});
        return txInvoker;
    }

}
