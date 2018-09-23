/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.root.config.scope;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年4月6日 下午2:09:48
 */
@Component
//@Primary
@RequestScope
//@Scope(value = SCOPE_PROTOTYPE, proxyMode = TARGET_CLASS)
public class RequestScopeExample implements ScopeExample {

    final static Logger LOGGER = LoggerFactory.getLogger(RequestScopeExample.class);

    private int id;
    private String name;

    @PreDestroy
    public void preDestroy() {
        LOGGER.debug("{}执行销毁前的资源回收任务", RequestScopeExample.class.getName());
    }

    /* (non-Javadoc)
     * @see ScopeExample#getScope()
     */
    @Override
    public String getScope() {
        return "request";
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
