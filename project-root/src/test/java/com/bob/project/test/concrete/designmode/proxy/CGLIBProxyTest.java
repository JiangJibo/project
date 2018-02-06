/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.designmode.proxy;

import com.bob.project.root.config.scope.SessionScopeExample;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @since 2017年4月13日 上午9:52:56
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CGLIBProxyTest {

	private DefaultListableBeanFactory beanFactory;
	private BeanDefinitionHolder originalHolder;
	private BeanDefinitionHolder proxyHolder;

	@Before
	public void init() {
		beanFactory = new DefaultListableBeanFactory();
		originalHolder = new BeanDefinitionHolder(new RootBeanDefinition(SessionScopeExample.class), "sessionScopeExample");
		proxyHolder = ScopedProxyUtils.createScopedProxy(originalHolder, beanFactory, true);
	}

	@Test
	public void testGetBeanName() {
		System.out.println(originalHolder.getBeanName());
		System.out.println(proxyHolder.getBeanName());
	}

	@Test
	public void testGetBean() {

	}

}
