/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.web.mvc.controller;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.bob.root.config.scope.RequestScopeExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年4月6日 下午4:19:40
 */
@RestController
@RequestMapping("/scope")
public class ScopeController {

    final static Logger LOGGER = LoggerFactory.getLogger(ScopeController.class);

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private RequestScopeExample requestScopeExample;

    /**
     * DEBUG 观察AOP动态代理内部流程
     * {@link org.springframework.aop.framework.CglibAopProxy}
     * {@link org.springframework.aop.framework.CglibAopProxy.DynamicAdvisedInterceptor#intercept(Object, Method, Object[], MethodProxy)}
     * {@link ReflectiveMethodInvocation#proceed()}
     */
    @PostConstruct
    public void init() {
        RequestScopeExample requestScope = beanFactory.getBean("requestScopeExample", RequestScopeExample.class);
        System.out.println(requestScope.getScope());
    }

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
        RequestScopeExample scope = (RequestScopeExample)request.getAttribute("requestScopeExample");
        System.out.println(requestScope == scope);
    }

}
