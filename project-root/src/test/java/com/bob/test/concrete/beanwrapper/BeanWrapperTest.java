/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.beanwrapper;

import java.beans.PropertyDescriptor;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;

/**
 * @since 2017年1月14日 上午10:40:42
 * @version $Id$
 * @author JiangJibo
 *
 */
public class BeanWrapperTest {

	private BeanWrapperImpl bw;

	@Before
	public void init() {
		bw = new BeanWrapperImpl(new Family());
		bw.registerCustomEditor(Member.class, new CustomMemberEditor());
	}

	@Test
	public void testGetPropertyDesc() {
		PropertyDescriptor desc = bw.getPropertyDescriptor("class");
		System.out.println(desc.getReadMethod());
		PropertyDescriptor[] descs = bw.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : descs) {
			System.out.println(propertyDescriptor.toString());
		}
	}

	@Test
	public void setPropertyDesc() {
		bw.setAutoGrowNestedPaths(true);
		PropertyDescriptor desc = bw.getPropertyDescriptor("member");
		bw.setPropertyValue("member", new Member());
		desc.setValue("name", "lanboal");
		Member member = ((Family) bw.getWrappedInstance()).getMember();
		System.out.println(member.getName());
	}

	@Test
	public void testRegisityPropertyEditor() {
		bw.setPropertyValue("member", "lanboal,28");
		Member member = (Member) bw.getPropertyValue("member");
		System.out.println(member.getName());
	}

	@Test
	public void testConvertedValue() {
		PropertyValue pv = new PropertyValue("member", "lanboal,28");

		Member member = (Member) bw.convertForProperty("bob,28", "member");

		pv.setConvertedValue(member);

		bw.setPropertyValue(pv);

		System.out.println(((Member) bw.getPropertyValue("member")).getName());
	}

	@Test
	public void testConvertProperty() {
		Member member = (Member) bw.convertForProperty("lanboal,28", "member");
		System.out.println(member.getName());
	}

	@Test
	public void testGrowPath() {
		// 当需要设置值烦人的的属性还不存在时，自动实例化对象
		bw.setAutoGrowNestedPaths(true);
		bw.setPropertyValue("members[0].name", "lanboal");
		Object obj = bw.getPropertyValue("members");
		System.out.println(obj.toString());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSetNestedNonPrimitiveField() {
		bw.setAutoGrowNestedPaths(true);
		// bw.setPropertyValue("members[0]", new Member("bob", 28));
		bw.setPropertyValue("members[0]", "bob,28");
		List<Member> members = (List<Member>) bw.getPropertyValue("members");
		if (!members.isEmpty()) {
			System.out.println(members.get(0).getName());
		}
	}

}
