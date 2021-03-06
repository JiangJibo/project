/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;

import com.bob.web.mvc.service.UserService;
import com.bob.web.config.model.User;
import com.bob.common.utils.userenv.process.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @since 2017年1月23日 上午11:14:07
 * @version $Id$
 * @author JiangJibo
 *
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public boolean login(@RequestBody User user, HttpServletRequest request) {
		user = userService.login(user);
		if (user == null) {
			return false;
		}
		AppUser.resideUserEnv(user);
		request.getSession().setMaxInactiveInterval(30);
		return true;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public boolean testUserEnvInjection(@RequestBody User user) {
		return userService.modifyUser(user);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.DELETE)
	public void logout() {
		System.out.println(AppUser.isLogin());
		AppUser.clearUserEnv();
		System.out.println(AppUser.isLogin());
	}

}
