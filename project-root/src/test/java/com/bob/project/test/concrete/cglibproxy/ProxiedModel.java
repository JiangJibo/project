/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.cglibproxy;

import com.bob.project.root.config.scope.RequestScopeExample;
import com.bob.project.utils.model.RootUser;
import org.springframework.context.annotation.Primary;

/**
 * @since 2017年8月1日 下午8:28:49
 * @version $Id$
 * @author JiangJibo
 *
 */
@Primary
public class ProxiedModel {

	private String msg;

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setMsgWithUser(RootUser user) {
		this.msg = user.getName();
	}

	public RootUser getUser() {
		return new RootUser();
	}

	public String invokeForAround(RootUser user) {
		System.out.println("执行Around方法");
		return user.getName();
	}

	public String invokeForAtWithin(RequestScopeExample example) {
		return "invokeForAtWithin";
	}

	public void invokeForThis() {
		throw new IllegalStateException("invokeForThis抛出异常");
	}

	public void invokeThrowing() {
		throw new IllegalArgumentException("invokeForThis抛出异常");
	}

}
