/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.root.injection;

import org.springframework.stereotype.Component;

/**
 * @since 2017年6月6日 下午2:37:46
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
// @Scope(value = "prototype")
// @Scope(value = "request")
public class Mother {

	private int id;
	private String name;
	private int age;

	private Child child;

	// @Autowired
	public Mother(Child child) {
		this.child = child;
	}

	public Mother() {
		this.id = 2;
		this.name = "Lily";
		this.age = 28;
	}

	// @Resource
	public void setChild(Child child) {
		this.child = child;
	}

	/**
	 * @return the child
	 */
	public Child getChild() {
		return child;
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
