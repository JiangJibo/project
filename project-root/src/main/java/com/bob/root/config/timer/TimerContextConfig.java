package com.bob.root.config.timer;

import com.bob.root.config.timer.quartz.QuartzTaskExample;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时器配置,推荐使用@Schedule的形式配置
 * 
 * @since 2017年4月4日 上午9:33:56
 * @version $Id$
 * @author JiangJibo
 *
 */
@Configuration
@ComponentScan()
@EnableScheduling
public class TimerContextConfig {

	/**
	 * 基于@Schedule方法的定时器配置
	 */

	/**
	 * Schedule线城池
	 * 
	 * @return
	 */
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler taskSchedule = new ThreadPoolTaskScheduler();
		taskSchedule.setPoolSize(5);
		return taskSchedule;
	}

	/**
	 * 
	 * Quartz定时器配置
	 * 
	 */

	// @Bean
	public MethodInvokingJobDetailFactoryBean taskInfo(QuartzTaskExample quartzTaskExample) {
		MethodInvokingJobDetailFactoryBean taskInfo = new MethodInvokingJobDetailFactoryBean();
		taskInfo.setTargetObject(quartzTaskExample);
		taskInfo.setTargetMethod("execute");
		return taskInfo;

	}

	// @Bean
	public CronTriggerFactoryBean quartzTrigger(JobDetail taskInfo) {
		CronTriggerFactoryBean quartzTrigger = new CronTriggerFactoryBean();
		quartzTrigger.setJobDetail(taskInfo);
		quartzTrigger.setCronExpression("0/20 * * * * ?");
		return quartzTrigger;

	}

	// @Bean
	public SchedulerFactoryBean registerQuartz(CronTrigger quartzTrigger) {
		SchedulerFactoryBean registerQuartz = new SchedulerFactoryBean();
		registerQuartz.setTriggers(quartzTrigger);
		return registerQuartz;

	}

}
