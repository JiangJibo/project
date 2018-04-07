package com.bob.root.config.timer;

import com.bob.root.config.timer.schedule.ScheduleTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时器配置,推荐使用@Schedule的形式配置
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2017年4月4日 上午9:33:56
 */
@Configuration
@EnableScheduling
public class TimerContextConfig {

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

    @Bean
    public ScheduleTask scheduleTask() {
        return new ScheduleTask();
    }

}
