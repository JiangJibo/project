/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 */
package com.bob.web.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bob.common.utils.userenv.process.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor拦截器,可以指定拦截的路径,详情见MappedInterceptor,match()方法匹配
 *
 * @since 2017年1月31日 下午12:44:57
 * @version $Id$
 * @author JiangJibo
 * @see org.springframework.web.servlet.handler.MappedInterceptor
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private final String[] includePatterns = {"/stus/**"};
    private final String[] excludePatterns = {"/stus/id"};

    final static Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(600);
        Boolean login = AppUser.getAppUser().isLogin();
        /*if (userName == null || login != Boolean.TRUE) {
			throw new IllegalStateException(request.getRequestURI() + "访问前需登录!");
		}*/
        LOGGER.info(this.getClass().getSimpleName() + "preHandle():\t" + request.getRequestURI());
        return true;
    }

    public String[] getIncludePatterns() {
        return includePatterns;
    }

    public String[] getExcludePatterns() {
        return excludePatterns;
    }

}
