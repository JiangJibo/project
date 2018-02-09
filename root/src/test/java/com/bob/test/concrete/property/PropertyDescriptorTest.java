/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.property;

import java.beans.PropertyDescriptor;

import com.bob.root.utils.model.RootUser;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * @since 2017年4月10日 下午7:32:06
 * @version $Id$
 * @author JiangJibo
 *
 */
public class PropertyDescriptorTest {

	@Test
	public void testGetProDesc() {
		PropertyDescriptor proDesc = BeanUtils.getPropertyDescriptor(RootUser.class, "name");
		System.out.println(proDesc.getPropertyType());
		System.out.println(proDesc.getReadMethod().getName());
		System.out.println(proDesc.getWriteMethod().getName());
	}

}
