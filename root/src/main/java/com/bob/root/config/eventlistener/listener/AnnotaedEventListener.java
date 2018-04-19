package com.bob.root.config.eventlistener.listener;

import javax.annotation.PostConstruct;

import com.bob.root.config.eventlistener.event.NonTypeBasedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import static org.springframework.context.support.AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;

/**
 * Spring事件广播监听器,如何发送广播在Test里
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2017年1月10日 下午2:54:20
 */
@Component
public class AnnotaedEventListener {

    final static Logger LOGGER = LoggerFactory.getLogger(AnnotaedEventListener.class);

    /**
     * 监听一次Http请求结束时产生的事件,可以获取当前请求的具体信息,比如请求处理时长
     *
     * @param event
     */
    @EventListener
    public void onRequestHandled(ServletRequestHandledEvent event) {
        LOGGER.debug("请求路径[{}],请求耗时[{}]毫秒", event.getRequestUrl(), event.getProcessingTimeMillis());
    }

    /**
     * 自定义Event,不继承{@code ApplicationContextEvent}
     *
     * @param event
     */
    @EventListener
    public void handleEvent(NonTypeBasedEvent event) {
        System.out.println(Thread.currentThread().getName());
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("AnnotaedEventListener handleEvent {}", event.getClass().getSimpleName());
        }
        event.print();
    }

    /**
     * SpringMVC容器初始化完成
     *
     * @param event
     */
    @EventListener
    public void onContextFreshed(ContextRefreshedEvent event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[{}]初始化完成 ", event.getApplicationContext());
        }
    }

}
