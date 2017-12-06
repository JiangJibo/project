/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.controller;

import com.bob.config.mvc.model.User;
import com.bob.test.config.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bob.config.mvc.userenv.model.LoginUser;
import com.bob.config.mvc.userenv.util.AppUser;
import com.bob.mvc.controller.UserController;

/**
 * @since 2017年1月23日 下午2:56:42
 * @version $Id$
 * @author JiangJibo
 *
 */
public class UserControllerTest extends BaseControllerTest {

	@Autowired
	private UserController userController;

	/* (non-Javadoc)
	 * @see com.bob.test.config.BaseControllerTest#init()
	 */
	@Override
	protected void init() {
		super.loginBefore = true;
		super.mappedController = userController;
		super.userName = "lanboal";
		super.password = "123456";
	}

	@Test
	public void testModifyUser() {
		User user = new User();
		user.setUserName("lanboal");
		this.putRequest(gson.toJson(user), "/users", "");
	}

	@Test
	public void logout() {
		this.deleteRequest("/users/logout", "");
	}

	@Test
	public void testGetSessionObj() {
		LoginUser loginUser = webApplicationContext.getBean(AppUser.USER_ENV_BEAN_NAME, LoginUser.class);
		System.out.println(loginUser);
	}

}
