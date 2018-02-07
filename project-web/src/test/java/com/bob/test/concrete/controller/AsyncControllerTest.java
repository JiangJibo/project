/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.controller;

import com.bob.web.mvc.controller.AsyncController;
import com.bob.test.config.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @since 2017年7月29日 下午3:01:32
 * @version $Id$
 * @author JiangJibo
 *
 */
public class AsyncControllerTest extends BaseControllerTest {

	@Autowired
	private AsyncController asyncController;

	/* (non-Javadoc)
	 * @see BaseControllerTest#init()
	 */
	@Override
	protected void init() {
		super.mappedController = asyncController;
	}

	@Test
	public void testDeferredResult() throws Exception {
		String result = this.getAsyncRequest("/async/deferred", "");
		System.out.println(result);
	}

}
