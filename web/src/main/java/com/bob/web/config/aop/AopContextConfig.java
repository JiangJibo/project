package com.bob.web.config.aop;

import com.bob.web.config.aop.advisor.TransactionAspectJAdvisor;
import com.bob.web.config.aop.advisor.UserEnvAspectJAdvisor;
import com.bob.web.config.aop.pointcut.PointcutArchitecture;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * AOP配置类
 *
 * @author wb-jjb318191
 * @create 2018-02-07 13:42
 */
@Configuration
@ComponentScan
public class AopContextConfig {

    @Bean
    public TransactionAspectJAdvisor transactionAspectJAdvisor() {
        return new TransactionAspectJAdvisor();
    }

    @Bean
    public UserEnvAspectJAdvisor userEnvAspectJAdvisor() {
        return new UserEnvAspectJAdvisor();
    }

    @Bean
    public PointcutArchitecture pointcutArchitecture() {
        return new PointcutArchitecture();
    }

}
