/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.designmode.observer;

import java.util.Observable;

/**
 * 学生,被观察者
 * 
 * @since 2017年6月24日 下午3:28:01
 * @version $Id$
 * @author JiangJibo
 *
 */
public class Student extends Observable {

	private final String name = "lanboal";
	private String badThingName;

	public void doBadThings(String name) {
		this.badThingName = name;
		System.out.println(this.name + name);
		this.setChanged(); // 设置状态发生了改变
		this.notifyObservers(); // 通知所有观察者
	}

	/**
	 * @return the badThingName
	 */
	public String getBadThingName() {
		return badThingName;
	}

	/**
	 * @param badThingName
	 *            the badThingName to set
	 */
	public void setBadThingName(String badThingName) {
		this.badThingName = badThingName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
