package com.bob.root.config;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;

import com.bob.common.utils.userenv.ann.UserEnv;
import com.bob.root.config.converter.String2DateConverter;
import com.bob.root.config.imports.CustomizeBeanDefinitionRegstrar;
import com.bob.root.config.imports.ImportBeanAnnotation;
import com.bob.root.config.injection.Child;
import com.bob.root.config.injection.Father;
import com.bob.root.config.injection.Mother;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年1月10日 上午8:52:37
 */
// @ImportResource(locations = {"classpath:com/bob/processor/servlet-processor.xml"})
@ComponentScan(basePackages = { "com.bob.root.config" },
    excludeFilters = {
    @Filter(type = FilterType.ANNOTATION, classes = { UserEnv.class }),
        @Filter(type = FilterType.REGEX, pattern = { "com.bob.processor.root" }) })
@Configuration
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "com.bob.root.config")
@ImportBeanAnnotation(value = "lanboal", telephone = "18758107760")
public class RootContextConfig {

    //@Bean
    @Scope("prototype")
    public Child child(Mother mother, Father father) {
        return new Child(mother, father);
    }

    @Bean
    public ConversionService conversionService() {
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new String2DateConverter());
        return conversionService;
    }

    @Bean
    public CustomizeBeanDefinitionRegstrar customizeBeanDefinitionRegstrar() {
        return new CustomizeBeanDefinitionRegstrar();
    }

    /**
     * Spring内部自定义线程池,可以对@Async注解以及Controler返回的Callable,WebAsyncTask和DeferredResult等Spring内异步线程的支持
     *
     * 当一个任务通过execute(Runnable)方法欲添加到线程池时：
     *
     * 如果此时线程池中的数量小于corePoolSize，即使线程池中的线程都处于空闲状态，也要创建新的线程来处理被添加的任务。
     *
     * 如果此时线程池中的数量等于 corePoolSize，但是缓冲队列 workQueue未满，那么任务被放入缓冲队列。
     *
     * 如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量小于maximumPoolSize，建新的线程来处理被添加的任务。
     *
     * 如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量等于maximumPoolSize，那么通过 handler所指定的策略来处理此任务。
     *
     * 也就是：处理任务的优先级为：核心线程corePoolSize、任务队列workQueue、最大线程 maximumPoolSize，如果三者都满了，使用handler处理被拒绝的任务。
     *
     * 当线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止。这样，线程池可以动态的调整池中的线程数。
     *
     * 拒绝策略：
     * {@link CallerRunsPolicy}          : 直接在主线程运行,也就是提交任务的线程, 哪个线程提交就哪个线程运行。此时就不是异步运行了，而是同步的
     * {@link AbortPolicy}               : 抛出异常
     * {@link DiscardOldestPolicy}       : 抛弃最老的那个任务,也就是Queue的头节点
     * {@link DiscardPolicy}             : 抛弃新加的任务,也就是什么都不做
     *
     * {@linkplain ThreadPoolExecutor#execute(Runnable)}
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 正常返回服务器内线程数量
        int threadNum = Runtime.getRuntime().availableProcessors() + 1;
        executor.setCorePoolSize(threadNum); // 线程池维护线程的最少数量
        executor.setMaxPoolSize(threadNum * 2); // 线程池维护线程的最大数量
        executor.setKeepAliveSeconds(300); // 空闲线程的最长保留时间,超过此时间空闲线程会被回收
        executor.setQueueCapacity(1000); // 线程池所使用的缓冲队列
        executor.setThreadNamePrefix("Spring-ThreadPool#");
        // rejection-policy：当线程池线程已达到最大值且任务队列也满了的情况下，如何处理新任务
        // CALLER_RUNS：哪个线程提交就哪个线程运行,同步运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
