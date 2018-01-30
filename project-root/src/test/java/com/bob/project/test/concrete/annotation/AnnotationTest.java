/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

/**
 * @since 2017年8月4日 上午9:09:27
 * @version $Id$
 * @author JiangJibo
 *
 */
public class AnnotationTest {

	private Method targetMethod;
	private After after; // 明确知道是哪个注解
	private Annotation ann; // 确定是注解，但是不确定类型

	@After(value = "lanboal")
	public void testAnn() {

	}

	@Before
	public void init() {
		targetMethod = ReflectionUtils.findMethod(AnnotationTest.class, "testAnn");
		after = targetMethod.getDeclaredAnnotation(After.class);
		ann = targetMethod.getDeclaredAnnotation(After.class);
	}

	@Test
	public void testEquals() throws NoSuchMethodException, SecurityException {
		System.out.println("after:" + after.toString() + ",\t ann:" + ann.toString());
		System.out.println(after == ann ? "after == ann" : "after != ann"); // After和Annotation这两个变量引用的是同一个对象
		System.out.println("After.annotationType:" + after.annotationType() + ",\t ann.annotationType:" + ann.annotationType());
		System.out.println("After.Class:" + after.getClass() + ",\t ann.Class:" + ann.getClass());
	}

	@Test
	public void testGetClass() {
		System.out.println("after.superClass:" + after.getClass().getSuperclass() + ", ann.superClass:" + ann.getClass().getSuperclass()); // 父类是Proxy
		System.out.println( // 当前Proxy$Num对象实现了@After注解
				"after.interfaces:" + after.getClass().getInterfaces()[0].getName() + ", ann.interfaces:" + ann.getClass().getInterfaces()[0].getName());
		System.out.println(after.getClass().getInterfaces()[0].getInterfaces()[0].getName()); // 体现@After(看做是一个接口)继承Annotation接口
	}

	@Test
	public void testGetMethod() throws Exception {
		Method after0 = after.getClass().getMethod("value"); // 代理对象Proxy$Num内的value()方法
		Method after1 = after.annotationType().getMethod("value"); // @Afte的value()方法
		Method ann0 = ann.getClass().getMethod("value"); // 代理对象Proxy$Num内的value()方法
		Method ann1 = ann.annotationType().getMethod("value"); // @Afte的value()方法
		System.out.println("[after0]:" + after0.toString() + ", [after1]:" + after1.toString());
		System.out.println("[ann0]:" + ann0.toString() + ", [ann1]:" + ann1.toString());
		System.out.println(after0 == ann0 ? "after0 == ann0" : "after0 != ann0");

		// 执行Proxy$Num.value()方法时会调用其代理的@After对象的value()方法,所以得到的结果和@After.value()的结果相同
		System.out.println("after0.invoke:" + after0.invoke(after) + ", after1.invoke:" + after1.invoke(after));
	}

	@Test
	public void testGetAnnType(){
		Class<? extends Annotation> aClass = ann.annotationType();
		System.out.println(aClass.getName());
	}

}
