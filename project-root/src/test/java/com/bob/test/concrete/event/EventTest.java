package com.bob.test.concrete.event;

import org.junit.Test;

import com.bob.config.mvc.eventlistener.event.NonTypeBasedEvent;
import com.bob.config.mvc.eventlistener.event.TypeBasedEvent;
import com.bob.test.config.BaseControllerTest;

/**
 * @since 2017年1月5日 上午11:05:27
 * @version $Id$
 * @author JiangJibo
 *
 */
public class EventTest extends BaseControllerTest {

	/* (non-Javadoc)
	 * @see com.bob.test.config.BaseControllerTest#init()
	 */
	@Override
	protected void init() {

	}

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
