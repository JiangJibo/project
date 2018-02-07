/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.intergrate.config.kafka.entity;

/**
 * @since 2017年7月1日 上午10:35:11
 * @version $Id$
 * @author JiangJibo
 *
 */
public class KafkaMessageEntity {

	private int id;
	private String name;
	private String sex;

	/**
	 * 
	 */
	public KafkaMessageEntity() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param sex
	 */
	public KafkaMessageEntity(int id, String name, String sex) {
		this.id = id;
		this.name = name;
		this.sex = sex;
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
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

}
