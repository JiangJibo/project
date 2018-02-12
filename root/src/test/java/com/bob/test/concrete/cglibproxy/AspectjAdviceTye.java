/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.cglibproxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectJAfterAdvice;
import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SimpleAspectInstanceFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * @since 2017年8月3日 上午9:05:15
 * @version $Id$
 * @author JiangJibo
 *
 */
  enum AspectjAdviceTye {

	BEFORE(Before.class, "Before") {

		@Override
		public AbstractAspectJAdvice buildAdvice(Method adviceMethod, Object original) throws Exception {
			Before ann = findAdviceAnnotation(Before.class, adviceMethod);
			if (ann == null) {
				return null;
			}
			AspectJExpressionPointcut expressionPointcut = generateExpressionPointcut(ann);
			checkPointcutMatching(original.getClass(), expressionPointcut);
			return new AspectJMethodBeforeAdvice(adviceMethod, expressionPointcut, new SimpleAspectInstanceFactory(adviceMethod.getDeclaringClass()));
		}

	},

	AROUND(Around.class, "Around") {

		@Override
		public AbstractAspectJAdvice buildAdvice(Method adviceMethod, Object original) throws Exception {
			Around ann = findAdviceAnnotation(Around.class, adviceMethod);
			if (ann == null) {
				return null;
			}
			AspectJExpressionPointcut expressionPointcut = generateExpressionPointcut(ann);
			checkPointcutMatching(original.getClass(), expressionPointcut);
			return new AspectJAroundAdvice(adviceMethod, generateExpressionPointcut(ann), new SimpleAspectInstanceFactory(adviceMethod.getDeclaringClass()));
		}
	},

	AFTER(After.class, "After") {

		@Override
		public AbstractAspectJAdvice buildAdvice(Method adviceMethod, Object original) throws Exception {
			After ann = findAdviceAnnotation(After.class, adviceMethod);
			if (ann == null) {
				return null;
			}
			AspectJExpressionPointcut expressionPointcut = generateExpressionPointcut(ann);
			checkPointcutMatching(original.getClass(), expressionPointcut);
			return new AspectJAfterAdvice(adviceMethod, expressionPointcut, new SimpleAspectInstanceFactory(adviceMethod.getDeclaringClass()));
		}
	},

	AFTERRETURNING(AfterReturning.class, "AfterReturning") {

		@Override
		public AbstractAspectJAdvice buildAdvice(Method adviceMethod, Object original) throws Exception {
			AfterReturning ann = findAdviceAnnotation(AfterReturning.class, adviceMethod);
			if (ann == null) {
				return null;
			}
			AspectJExpressionPointcut expressionPointcut = generateExpressionPointcut(ann);
			checkPointcutMatching(original.getClass(), expressionPointcut);
			AspectJAfterReturningAdvice advice = new AspectJAfterReturningAdvice(adviceMethod, expressionPointcut,
					new SimpleAspectInstanceFactory(adviceMethod.getDeclaringClass()));
			if (StringUtils.hasText(ann.returning())) {
				advice.setReturningName(ann.returning());
			}
			return advice;
		}
	},

	AFTERTHROWING(AfterThrowing.class, "AfterThrowing") {

		@Override
		public AbstractAspectJAdvice buildAdvice(Method adviceMethod, Object original) throws Exception {
			AfterThrowing ann = findAdviceAnnotation(AfterThrowing.class, adviceMethod);
			if (ann == null) {
				return null;
			}
			AspectJExpressionPointcut expressionPointcut = generateExpressionPointcut(ann);
			checkPointcutMatching(original.getClass(), expressionPointcut);
			AspectJAfterThrowingAdvice advice = new AspectJAfterThrowingAdvice(adviceMethod, expressionPointcut,
					new SimpleAspectInstanceFactory(adviceMethod.getDeclaringClass()));
			if (StringUtils.hasText(ann.throwing())) {
				advice.setThrowingName(ann.throwing());
			}
			return advice;
		}
	};

	private final Class<? extends Annotation> adviceClass;
	private final String label;

	private static final Map<Class<? extends Annotation>, AspectjAdviceTye> VALUES = new LinkedHashMap<Class<? extends Annotation>, AspectjAdviceTye>();

	static {
		for (AspectjAdviceTye ann : AspectjAdviceTye.values()) {
			VALUES.put(ann.adviceClass, ann);
		}
	}

	/**
	 * @param adviceMethod
	 * @return
	 */
	public static AspectjAdviceTye valueOf(Method adviceMethod) {
		for (Entry<Class<? extends Annotation>, AspectjAdviceTye> entry : VALUES.entrySet()) {
			Annotation adviceAnn = findAdviceAnnotation(entry.getKey(), adviceMethod);
			if (adviceAnn != null) {
				return entry.getValue();
			}
		}
		throw new IllegalArgumentException("指定的方法[" + adviceMethod + "]上不含有切面注解");
	}

	/**
	 * @param annClass
	 * @param method
	 * @return
	 */
	private static <A extends Annotation> A findAdviceAnnotation(Class<A> annClass, Method method) {
		return method.getDeclaredAnnotation(annClass);
	}

	/**
	 * 根据{@code @Before}等注解生成切入点
	 * 
	 * @param aspectAnn
	 * @return
	 * @throws Exception
	 */
	private static AspectJExpressionPointcut generateExpressionPointcut(Annotation aspectAnn) throws Exception {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(AspectPointcutConfig.class, new String[0], new Class<?>[0]);
		pointcut.setExpression(AnnotationUtils.getValue(aspectAnn).toString());
		return pointcut;
	}

	/**
	 * 校验被代理类是否契合切入点
	 * 
	 * @param pointcut
	 */
	private static void checkPointcutMatching(Class<?> proxyClass, AspectJExpressionPointcut pointcut) {
		Assert.state(pointcut.matches(proxyClass), "Pointcut的Expression必须契合被代理类[" + proxyClass.getName() + "]");
		List<Method> matchedMethod = new ArrayList<Method>();
		ReflectionUtils.doWithMethods(proxyClass, (method) -> {
			if (pointcut.matches(method, proxyClass)) {
				matchedMethod.add(method);
			}
		});
		Assert.state(!matchedMethod.isEmpty(), "被代理类[" + proxyClass.getName() + "]没有方法契合ExpressionPointcut");
	}

	/**
	 * 创建代理切面通知
	 * 
	 * @param adviceMethod
	 * @return
	 * @throws Exception
	 */
	public abstract AbstractAspectJAdvice buildAdvice(Method adviceMethod, Object original) throws Exception;

	/**
	 * @param annClass
	 * @param label
	 */
	private AspectjAdviceTye(Class<? extends Annotation> annClass, String label) {
		this.adviceClass = annClass;
		this.label = label;
	}

	/**
	 * @return the adviceClass
	 */
	public Class<? extends Annotation> getAdviceClass() {
		return adviceClass;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
