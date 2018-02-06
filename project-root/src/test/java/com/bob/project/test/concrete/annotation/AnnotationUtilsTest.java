/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.annotation;

import java.lang.reflect.Method;
import java.util.Set;

import com.bob.project.utils.model.RootUser;
import org.junit.Test;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.web.bind.annotation.ModelAttribute;


/**
 * @since 2017年3月7日 下午9:12:24
 * @version $Id$
 * @author JiangJibo
 *
 */
public class AnnotationUtilsTest {

	public void modelParameter(@ModelAttribute RootUser stu) {
		System.out.println(stu);
	}

	@ModelAttribute
	public void modelMethod(RootUser stu) {
		System.out.println(stu);
	}

	@Test
	public void getModelMethods() {
		Set<Method> methods = MethodIntrospector.selectMethods(AnnotationUtilsTest.class, MODEL_ATTRIBUTE_METHODS);
		for (Method method : methods) {
			System.out.println(method.getName());

		}
	}

	public static final MethodFilter MODEL_ATTRIBUTE_METHODS = new MethodFilter() {

		@Override
		public boolean matches(Method method) {
			return (AnnotationUtils.findAnnotation(method, ModelAttribute.class) != null);
		}
	};

}
