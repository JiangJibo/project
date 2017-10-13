/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.async;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter;

/**
 * @since 2017年6月15日 下午3:15:10
 * @version $Id$
 * @author JiangJibo
 *
 */
public class AsyncDeferredResultInterceptor extends DeferredResultProcessingInterceptorAdapter {

	final static Logger LOGGER = LoggerFactory.getLogger(AsyncDeferredResultInterceptor.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter#beforeConcurrentHandling(org.springframework.web.context.request.NativeWebRequest, org.springframework.web.context.request.async.DeferredResult)
	 */
	@Override
	public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
		LOGGER.debug("请求路径为:{}", request.getNativeRequest(HttpServletRequest.class).getRequestURI());
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter#handleTimeout(org.springframework.web.context.request.NativeWebRequest, org.springframework.web.context.request.async.DeferredResult)
	 */
	@Override
	public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
		HttpServletRequest httpRequest = request.getNativeRequest(HttpServletRequest.class);
		LOGGER.debug("请求的路径为:[{}]", httpRequest.getRequestURI());
		return true;
	}

}
