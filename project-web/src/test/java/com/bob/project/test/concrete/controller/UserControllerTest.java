/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.controller;

import com.bob.project.mvc.controller.UserController;
import com.bob.project.test.config.BaseControllerTest;
import com.bob.project.web.config.model.User;
import com.bob.project.web.config.userenv.model.LoginUser;
import com.bob.project.web.config.userenv.util.AppUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
	 * @see BaseControllerTest#init()
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
