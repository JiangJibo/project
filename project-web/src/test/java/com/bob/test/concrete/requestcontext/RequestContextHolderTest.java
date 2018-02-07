/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.requestcontext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bob.test.config.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @since 2017年4月7日 下午1:47:05
 * @version $Id$
 * @author JiangJibo
 *
 */
public class RequestContextHolderTest extends BaseControllerTest {

	@Autowired
	private HttpServletRequest request;

	@Test
	public void testGetSession() {
		HttpSession session1 = request.getSession();
		HttpSession session0 = request.getSession(false);
	}

	/* (non-Javadoc)
	 * @see BaseControllerTest#init()
	 */
	@Override
	protected void init() {

	}

}
