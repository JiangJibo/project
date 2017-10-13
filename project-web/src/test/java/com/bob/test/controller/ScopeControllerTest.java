package com.bob.test.controller;

import com.bob.test.config.BaseControllerTest;
import org.junit.Test;


/**
 * @since 2017年4月6日 下午4:22:36
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ScopeControllerTest extends BaseControllerTest {

	/* (non-Javadoc)
	 * @see com.bob.test.config.BaseControllerTest#init()
	 */
	@Override
	protected void init() {

	}

	@Test
	public void testRequestScope() {
		this.getRequest("/scope", "");
	}

}
