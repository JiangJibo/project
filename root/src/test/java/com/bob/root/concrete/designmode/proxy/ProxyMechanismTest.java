/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;

import org.junit.Test;

/**
 * 动态代理演示
 * 
 * @since 2017年5月31日 上午10:15:37
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ProxyMechanismTest {

	@Test
	public void testDynamicProxy() throws FileNotFoundException {
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true"); // 将动态代理生成的字节码文件保存到磁盘
		Subject subject = DynProxyFactory.getSubjectInstance(RealSubject.class);
		subject.print();
		((Handler) subject).handler(new Message(1, "今天的天气真好", LocalDateTime.now()));
		onAfter(subject);
		Handler handler = DynProxyFactory.getSubjectInstance(RealSubject.class);
		handler.handler(new Message(1, "今天的天气真好", LocalDateTime.now()));
	}

	public void onAfter(Object proxyObj) throws FileNotFoundException {
		String simpleClassName = proxyObj.getClass().getSimpleName();
		FileOutputStream file = new FileOutputStream(simpleClassName + ".class");
		System.out.println(new File(simpleClassName + ".class").getAbsolutePath());
	}

	/**
	 * 可以尝试将Handler接口定义成public,然后看看代理生成的类的名称
	 */
	@Test
	public void testClassName() {
		Subject subject = DynProxyFactory.getSubjectInstance(RealSubject.class);
		System.out.println(subject.getClass().getName());
		Handler handler = DynProxyFactory.getSubjectInstance(RealSubject.class);
		System.out.println(handler.getClass().getName());
		System.out.println(Modifier.isFinal(handler.getClass().getModifiers()));
	}

	@Test
	public void testSuperClasses() {
		Subject subject = DynProxyFactory.getSubjectInstance(RealSubject.class);
		Class<?> clazz = subject.getClass().getSuperclass();
		System.out.println(clazz.getName());
	}

	@Test
	public void testInterfaces() {
		Subject subject = DynProxyFactory.getSubjectInstance(RealSubject.class);
		Class<?>[] interfaces = subject.getClass().getInterfaces();
		for (Class<?> clazz : interfaces) {
			System.out.println(clazz.getName());
		}
	}

}
