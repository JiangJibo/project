/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.config.mvc.async;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptorAdapter;

/**
 * 针对Spring异步请求超时如何处理
 *
 * @since 2017年6月9日 下午7:00:59
 * @version $Id$
 * @author JiangJibo
 *
 */
public class AsyncCallableInterceptor extends CallableProcessingInterceptorAdapter {

    private static final Object TIME_OUT_RESULT = "time_out";

    final static Logger LOGGER = LoggerFactory.getLogger(AsyncCallableInterceptor.class);

    @Override
    public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {
        LOGGER.debug("请求路径为:{}", request.getNativeRequest(HttpServletRequest.class).getRequestURI());
    }

    @Override
    public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
        HttpServletRequest httpRequest = request.getNativeRequest(HttpServletRequest.class);
        LOGGER.debug("请求的路径为:[{}]", httpRequest.getRequestURI());
        return TIME_OUT_RESULT;
    }

}
