/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.designmode.clone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.bob.config.mvc.model.User;
import com.google.gson.Gson;

/**
 * @since 2017年6月22日 下午12:02:19
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CloneEntity implements Cloneable, Serializable {

	private static final long serialVersionUID = 2327283592700889875L;

	public static final Gson Gson = new Gson();

	private int id = 1;
	private String name = "lanboal";

	private User user = new User("lanboal", "123456");

	public CloneEntity() {
		System.out.println("执行构造函数");
		this.id = 2;
		this.name = "bob";
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * 通过流深拷贝
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public CloneEntity deepCloneByStream() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bao);
		oos.writeObject(this);
		ByteArrayInputStream bis = new ByteArrayInputStream(bao.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return (CloneEntity) ois.readObject();
	}

	/**
	 * 通过Gson深拷贝
	 * 
	 * @return
	 */
	public CloneEntity deepCloneByGson() {
		return Gson.fromJson(Gson.toJson(this), CloneEntity.class);
	}

}
