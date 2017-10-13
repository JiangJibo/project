/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.resource;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bob.config.mvc.scope.ScopeExample;

/**
 * @since 2017年4月12日 下午3:28:47
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class ResourceAnnotationExample {

	final static Logger LOGGER = LoggerFactory.getLogger(ResourceAnnotationExample.class);

	@Autowired
	private ScopeExample scopeExample;

	@Resource
	public void getRequestScope(ScopeExample sessionScopeExample) {
		LOGGER.debug("[{}]作用域为{}", sessionScopeExample.getClass().getName(), sessionScopeExample.getScope());
	}

}
