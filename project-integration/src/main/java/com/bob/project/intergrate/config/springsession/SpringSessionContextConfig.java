package com.bob.project.intergrate.config.springsession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Spring-session整合配置类
 *
 * @author wb-jjb318191
 * @create 2018-01-25 15:06
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 120)
public class SpringSessionContextConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSessionContextConfig.class);

    /**
     * Spring-session-redis执行线程池
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler springSessionRedisTaskExecutor() {
        ThreadPoolTaskScheduler taskSchedule = new ThreadPoolTaskScheduler();
        taskSchedule.setPoolSize(3);
        return taskSchedule;
    }

    /**
     * 自定义返回给前端的Cookie的项目根路径
     *
     * @return
     */
    @Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookiePath("/");
        return defaultCookieSerializer;
    }

    /**
     * Redis内session过期事件监听
     *
     * @param expiredEvent
     */
    @EventListener
    public void onSessionExpired(SessionExpiredEvent expiredEvent) {
        String sessionId = expiredEvent.getSessionId();
        LOGGER.info(expiredEvent.getSession().getAttribute("user"));
        LOGGER.info("[{}]session过期", sessionId);
    }

    /**
     * Redis内session删除事件监听
     *
     * @param deletedEvent
     */
    @EventListener
    public void onSessionDeleted(SessionDeletedEvent deletedEvent) {
        String sessionId = deletedEvent.getSessionId();
        LOGGER.info(deletedEvent.getSession().getAttribute("user"));
        LOGGER.info("删除session[{}]", sessionId);
    }

    /**
     * Redis内session保存事件监听
     *
     * @param createdEvent
     */
    @EventListener
    public void onSessionCreated(SessionCreatedEvent createdEvent) {
        String sessionId = createdEvent.getSessionId();
        LOGGER.info(createdEvent.getSession().getAttribute("user"));
        LOGGER.info("保存session[{}]", sessionId);
    }

}
