package com.bob.config.root;

import java.util.concurrent.ThreadPoolExecutor;

import com.bob.config.root.converter.String2DateConverter;
import com.bob.config.root.injection.Child;
import com.bob.config.root.injection.Father;
import com.bob.config.root.injection.Mother;
import com.bob.config.root.registrar.CustomizeBeanDefinitionRegstrar;
import com.bob.config.root.registrar.ImportedBeanRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @since 2017年1月10日 上午8:52:37
 * @version $Id$
 * @author JiangJibo
 *
 */
// @ImportResource(locations = {"classpath:com/bob/config/servlet-config.xml"})
// @PropertySource(value = "classpath:com/bob/config/log/log4j.properties", ignoreResourceNotFound =true)
/*@ComponentScan(basePackages = { "com.bob.config.root" }, excludeFilters = { @Filter(type = FilterType.ANNOTATION, classes = { UserEnv.class }),
		@Filter(type = FilterType.REGEX, pattern = { "com.bob.config.root" }) })*/
@Configuration
@EnableAsync
@ComponentScan(basePackages = "com.bob.config.root")
@Import({ DataAccessContextConfig.class, RedisCacheContextConfig.class })
@ImportedBeanRegistry(value = "lanboal", telephone = "18758107760")
public class RootContextConfig {

	@Bean
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
	 * {@linkplain ThreadPoolExecutor#execute(Runnable)}
	 *
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10); // 线程池维护线程的最少数量
		executor.setMaxPoolSize(20); // 线程池维护线程的最大数量
		executor.setKeepAliveSeconds(300); // 空闲线程的最长保留时间,超过此时间空闲线程会被回收
		executor.setQueueCapacity(30); // 线程池所使用的缓冲队列
		executor.setThreadNamePrefix("Spring-ThreadPool#");
		// rejection-policy：当线程池线程已达到最大值且任务队列也满了的情况下，如何处理新任务
		// CALLER_RUNS：这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

}
