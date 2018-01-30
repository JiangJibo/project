/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.config.mvc.scope;

import org.springframework.stereotype.Component;

/**
 * @since 2017年4月6日 下午4:25:55
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
// @Scope("session")
public class SessionScopeExample implements ScopeExample {

	private int id;
	private String name;

	/* (non-Javadoc)
	 * @see ScopeExample#getScope()
	 */
	@Override
	public String getScope() {
		return "session";
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
