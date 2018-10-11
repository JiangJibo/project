/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.config.resource;

import javax.annotation.Resource;

import com.bob.root.config.scope.ScopeExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @since 2017年4月12日 下午3:28:47
 * @version $Id$
 * @author JiangJibo
 *
 */
//@Component
public class ResourceAnnotationExample {

	final static Logger LOGGER = LoggerFactory.getLogger(ResourceAnnotationExample.class);

	@Autowired
	private ScopeExample scopeExample;

	@Resource
	public void getRequestScope(ScopeExample sessionScopeExample) {
		LOGGER.debug("[{}]作用域为{}", sessionScopeExample.getClass().getName(), sessionScopeExample.getScope());
	}

}
