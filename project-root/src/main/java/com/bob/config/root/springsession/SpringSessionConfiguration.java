package com.bob.config.root.springsession;

import com.bob.mvc.model.BankUser;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;
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
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30)
public class SpringSessionConfiguration {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSessionConfiguration.class);

    /**
     * Redis内session过期事件监听
     *
     * @param expiredEvent
     */
    @EventListener
    public void onSessionExpired(SessionExpiredEvent expiredEvent) {
        String sessionId = expiredEvent.getSessionId();
        ExpiringSession session = expiredEvent.getSession();
        BankUser user = session.getAttribute("user");
        LOGGER.info(GSON.toJson(user));
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
        ExpiringSession session = deletedEvent.getSession();
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
        ExpiringSession session = createdEvent.getSession();
        LOGGER.info("保存session[{}]", sessionId);
    }

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

    @Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookiePath("/");
        return defaultCookieSerializer;
    }

}
