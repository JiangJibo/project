/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.genericType;

import java.util.Date;

import com.bob.project.root.config.converter.String2DateConverter;
import com.bob.project.root.config.factorybean.RootUserFactory;
import com.bob.project.utils.model.RootUser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.converter.Converter;

/**
 * @since 2017年3月5日 下午8:42:46
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ReslovableTypeTest {

	private Converter<String, Date> cov;

	private FactoryBean<RootUser> fb;

	@Before
	public void init() {
		cov = new String2DateConverter();
		fb = new RootUserFactory();
	}

	@Test
	public void testReslovableType() {
		ResolvableType type = ResolvableType.forClass(fb.getClass());
		// class com.bob.student.entity.Student
		System.out.println(type.as(FactoryBean.class).getGeneric(0).resolve());
	};

	@Test
	public void testGetConverter() {
		ResolvableType type = ResolvableType.forClass(cov.getClass()).as(Converter.class);
		System.out.println(type.resolveGeneric(0)); // class java.lang.String
		System.out.println(type.getGeneric(1).resolve()); // class java.util.Date
	}

	@Test
	public void getInterfaceType() {
		ResolvableType type = ResolvableType.forClass(cov.getClass()).getInterfaces()[0];
		System.out.println(type.getGeneric(0).resolve()); // class java.lang.String
		System.out.println(type.getGeneric(1).resolve()); // class java.util.Date
	}
}
