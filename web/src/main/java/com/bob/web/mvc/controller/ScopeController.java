/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bob.root.config.scope.RequestScopeExample;

/**
 * @since 2017年4月6日 下午4:19:40
 * @version $Id$
 * @author JiangJibo
 *
 */
@RestController
@RequestMapping("/scope")
public class ScopeController {

	final static Logger LOGGER = LoggerFactory.getLogger(ScopeController.class);

	@Autowired
	private BeanFactory beanFactory;

	/**
	 * 验证request scope的Bean对象是否存储在HttpServletRequest中
	 * 
	 * @param request
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void testRequestScope(HttpServletRequest request) {
		RequestScopeExample requestScope = beanFactory.getBean("requestScopeExample", RequestScopeExample.class);
		requestScope.setId(1);
		requestScope.setName("requestScope");
		RequestScopeExample scope = (RequestScopeExample) request.getAttribute("requestScopeExample");
		System.out.println(requestScope == scope);
	}

}
