package com.bob.root.concrete.event;

import javax.servlet.http.HttpServletRequest;

import com.bob.root.config.eventlistener.event.NonTypeBasedEvent;
import com.bob.root.config.eventlistener.event.TypeBasedEvent;
import com.bob.root.config.TestContextConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年1月5日 上午11:05:27
 */
public class EventTest extends TestContextConfig {

    @Autowired
    private HttpServletRequest request;

    @Test
    public void testTypeBasedEvent() {
        System.out.println(Thread.currentThread().getName());
        webApplicationContext.publishEvent(new TypeBasedEvent(webApplicationContext));
    }

    @Test
    public void testNonTypeBasedEvent() {
        System.out.println(Thread.currentThread().getName());
        webApplicationContext.publishEvent(new NonTypeBasedEvent());
    }

}
