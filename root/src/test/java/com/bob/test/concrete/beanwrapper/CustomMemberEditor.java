/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.beanwrapper;

import java.beans.PropertyEditorSupport;

/**
 * @since 2017年1月14日 上午10:44:03
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CustomMemberEditor extends PropertyEditorSupport {

	/* (non-Javadoc)
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String[] vars = text.split(",");
		Member member = new Member();
		member.setName(vars[0]);
		member.setAge(Integer.valueOf(vars[1]));
		super.setValue(member);
	}

}
