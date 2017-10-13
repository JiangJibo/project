/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.staticmethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import com.bob.config.root.converter.String2DateConverter;

/**
 * @since 2017年1月15日 下午3:49:57
 * @version $Id$
 * @author JiangJibo
 *
 */
public class StaticMethodTest {

	public static ConversionService conversionService() {
		GenericConversionService conversionService = new GenericConversionService();
		conversionService.addConverter(new String2DateConverter());
		return conversionService;
	}

	/**
	 * 检测static方法执行时不需要指定对象
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@Test
	public void invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = BeanUtils.findDeclaredMethod(StaticMethodTest.class, "conversionService", new Class<?>[] {});
		System.out.println(Modifier.isStatic(method.getModifiers()));
		Object obj = method.invoke(null, (Object[]) null);
		System.out.println(obj instanceof ConversionService);
	}

}
