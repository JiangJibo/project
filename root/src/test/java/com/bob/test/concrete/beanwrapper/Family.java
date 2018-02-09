/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.beanwrapper;

/**
 * @since 2017年1月14日 上午10:42:05
 * @version $Id$
 * @author JiangJibo
 *
 */
public class Family {

	public static final String serId = "Unique-1234";

	private Member member;

	/**
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * @param member
	 *            the member to set
	 */
	public void setMember(Member member) {
		this.member = member;
	}

}
