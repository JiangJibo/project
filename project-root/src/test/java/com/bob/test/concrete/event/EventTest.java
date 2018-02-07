package com.bob.test.concrete.event;

import com.bob.root.config.eventlistener.event.NonTypeBasedEvent;
import com.bob.root.config.eventlistener.event.TypeBasedEvent;
import com.bob.test.config.BaseControllerTest;
import org.junit.Test;

/**
 * @since 2017年1月5日 上午11:05:27
 * @version $Id$
 * @author JiangJibo
 *
 */
public class EventTest extends BaseControllerTest {


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
