/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.clazz;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @since 2017年7月26日 下午3:07:49
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ClassTest {

	public void voidMethod() {
		System.out.println("执行Void方法");
	}

	@Test
	public void testGetVoidMethodReturn() {
		Method method = ReflectionUtils.findMethod(ClassTest.class, "voidMethod");
		Object obj = ReflectionUtils.invokeMethod(method, new ClassTest());
		System.out.println(obj);
	}

	@Test
	public void testGetInnerClass() {
		Class<?>[] classes = Entity.class.getDeclaredClasses();
		for (Class<?> clazz : classes) {
			System.out.println(
					"Class.Name = " + clazz.getName() + ", Class.SimpleName = " + clazz.getSimpleName() + ", Class.Package = " + clazz.getPackage().getName());
		}
	}

	@Test
	public void getMostSpecificMethod() throws Exception {
		Method orignalMethod = Handler.class.getMethod("handler");
		Method targetMethod = ClassUtils.getMostSpecificMethod(orignalMethod, Entity.class);
		System.out.println("orignalMethod:" + orignalMethod + " , targetMethod:" + targetMethod);
		Entity entity = new Entity();
		orignalMethod.invoke(entity);
		targetMethod.invoke(entity);
	}

	@Test
	public void testGetInetAdress() throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getLocalHost();
		System.out.println(inetAddress.getCanonicalHostName());
	}

	@Test
	public void testArrayList(){
		List<String> list = new ArrayList<String>();
		list.add(null);
		list.add(null);
		System.out.println(list.size());
		System.out.println(list.toString());
	}

	/**
	 * 测试多层三目运算符反悔空指针异常
	 */
	@Test
	public void test3m(){
		String s1 = "bb";
		String s2 = "aa";
		Integer result = (s1 == null && s2 == null) ? 0 : s1 == null ? -1 : s2 == null ? 1 : null;
		System.out.println(result);
	}


}
