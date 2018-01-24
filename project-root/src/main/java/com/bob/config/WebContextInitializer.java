package com.bob.config;

import javax.servlet.Filter;

import com.bob.config.mvc.MvcContextConfig;
import com.bob.config.root.RootContextConfig;
import com.bob.config.root.initializer.RootContextInitializer;
import com.bob.config.root.initializer.ServletContextInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 对应着框架内部的web.xml文件内的配置,系统入口
 *
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月5日 下午4:19:09
 */
public class WebContextInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {RootContextConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {MvcContextConfig.class};
    }

    @Override
    protected ApplicationContextInitializer<?>[] getRootApplicationContextInitializers() {
        return new ApplicationContextInitializer<?>[] {new RootContextInitializer()};
    }

    @Override
    protected ApplicationContextInitializer<?>[] getServletApplicationContextInitializers() {
        return new ApplicationContextInitializer<?>[] {new ServletContextInitializer()};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {new CharacterEncodingFilter("UTF-8", true), new DelegatingFilterProxy()};
    }

}
