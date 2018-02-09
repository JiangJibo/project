/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.config.injection;

import org.springframework.stereotype.Component;

/**
 * @since 2017年7月26日 下午8:47:41
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class Father {

	private int id;
	private String name;
	private int age;

	public Father() {
		this.id = 1;
		this.name = "lanboal";
		this.age = 30;
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

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

}
