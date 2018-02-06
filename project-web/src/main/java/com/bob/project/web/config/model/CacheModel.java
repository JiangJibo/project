/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.web.config.model;

import java.io.Serializable;

import javax.validation.constraints.Min;

import com.bob.project.web.config.userenv.ann.UserEnv;
import org.springframework.stereotype.Repository;


/**
 * @since 2016年12月6日 上午10:36:13
 * @version $Id$
 * @author JiangJibo
 *
 */
@UserEnv
@Repository
public class CacheModel implements Serializable {

	private static final long serialVersionUID = 3758896496338455420L;

	private Integer id;
	@UserEnv("userName")
	private String name;
	@Min(value = 30, message = "com.bob.age.min")
	private Integer age;
	private String telephone;
	private String adress;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return
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
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}

	/**
	 * @param adress
	 *            the adress to set
	 */
	public void setAdress(String adress) {
		this.adress = adress;
	}

}
