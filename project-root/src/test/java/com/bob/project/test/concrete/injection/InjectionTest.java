/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.injection;

import java.util.List;

import com.bob.project.root.config.injection.Child;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bob.project.root.config.injection.Father;
import com.bob.project.root.config.injection.Mother;
import com.bob.project.test.config.BaseControllerTest;
import com.google.gson.Gson;

/**
 * @since 2017年6月6日 下午2:42:44
 * @version $Id$
 * @author JiangJibo
 *
 */
public class InjectionTest extends BaseControllerTest {

	@Autowired
	private BeanFactory beanFactory;

	private Gson gson = new Gson();


	@Test
	@SuppressWarnings("unchecked")
	public void testAutowired() {

		List<Mother> mothers = beanFactory.getBean("mother", List.class);
		System.out.println(mothers.get(0).getChild().getName());
		Mother mother02 = beanFactory.getBean("mother", Mother.class);
		System.out.println(mother02.toString());

		Child child = beanFactory.getBean(Child.class, new Object[] { "小明", 14, "男" });

		Mother mother1 = (Mother) beanFactory.getBean("mother", child);
		System.out.println(mother1.toString());

		// Mother mother2 = beanFactory.getBean(Mother.class, new Child("小红", 12, "女"));
		Mother mother2 = (Mother) beanFactory.getBean("mother", new Child("小红", 12, "女"));
		System.out.println(mother2.toString());
	}

	@Test
	public void testGetBean0() {
		Child child = beanFactory.getBean("child", Child.class);
		System.out.println(child.toString());
		System.out.println(gson.toJson(child));
		child = beanFactory.getBean(Child.class, new Object[] { "小明", 14, "男" });
		System.out.println(child.toString());
		System.out.println(gson.toJson(child));
	}

	@Test
	public void testGetBean1() {
		Father father = new Father();
		father.setId(100);
		Mother mother = new Mother();
		mother.setName("Lucy");
		Child child;
		// child = (Child) beanFactory.getBean("child", mother, father);
		// child = (Child) beanFactory.getBean("child", mother); // 错误
		child = beanFactory.getBean("child", Child.class);
		System.out.println(gson.toJson(child));
	}


}
