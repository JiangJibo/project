/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.web.concrete.requestcontext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bob.web.config.BaseControllerTest;
import com.bob.web.config.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年4月7日 下午1:47:05
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
     * @see TestContextConfig#init()
     */
    @Override
    protected void init() {

    }

}
