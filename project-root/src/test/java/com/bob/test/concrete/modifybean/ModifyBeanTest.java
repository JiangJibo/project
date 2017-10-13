/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.modifybean;

import com.bob.config.mvc.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;

import com.bob.test.config.BaseControllerTest;

/**
 * @since 2017年1月23日 上午10:30:05
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ModifyBeanTest extends BaseControllerTest {

	private BeanFactory beanFactory;

	@Before
	public void init() {
		beanFactory = this.webApplicationContext.getAutowireCapableBeanFactory();
	}

	@Test
	public void testModify() {
		User user = beanFactory.getBean("user", User.class);
		System.out.println(user.getUserName());
		user.setUserName("named by modify");
		User uu = (User) beanFactory.getBean("user");
		System.out.println(uu.getUserName());
	}

}
