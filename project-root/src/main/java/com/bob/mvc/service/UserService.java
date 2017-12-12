/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.mvc.service;

import com.bob.config.mvc.model.User;

/**
 * @since 2017年1月23日 上午11:12:55
 * @version $Id$
 * @author JiangJibo
 *
 */
public interface UserService {

	/**
	 * 登陆
	 * 
	 * @param user
	 * @return
	 */
	public User login(User user);

	/**
	 * 修改用户的信息
	 * 
	 * @param user
	 * @return
	 */
	public boolean modifyUser(User user);



}
