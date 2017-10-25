/**
 * Copyright(C) 2016 Massabot Technology Co. Ltd. All rights reserved.
 */
package com.bob.config.mvc;

import java.nio.charset.Charset;
import java.util.List;

import com.bob.config.mvc.async.AsyncCallableInterceptor;
import com.bob.config.mvc.async.AsyncDeferredResultInterceptor;
import com.bob.config.mvc.exception.CustomizedExceptionResolver;
import com.bob.config.mvc.formatter.StudentFormatter;
import com.bob.config.mvc.interceptor.LoginInterceptor;
import com.bob.config.mvc.scanfilter.MvcContextScanExcludeFilter;
import com.bob.config.mvc.timer.TimerContextConfig;
import com.bob.config.mvc.userenv.AppUserContextConfig;
import org.hibernate.validator.HibernateValidator;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @since 2016年12月5日 下午4:20:35
 * @version $Id$
 * @author JiangJibo
 *
 */
@Configuration
@EnableAsync
@EnableWebMvc
@MapperScan("com.bob.mvc.mapper")
@ComponentScan(basePackages = {"com.bob.mvc"}, basePackageClasses = {MvcContextConfig.class}, excludeFilters = {
    @Filter(type = FilterType.CUSTOM, classes = {MvcContextScanExcludeFilter.class})})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({ AppUserContextConfig.class, TimerContextConfig.class})
public class MvcContextConfig extends WebMvcConfigurerAdapter {

    final static Logger LOGGER = LoggerFactory.getLogger(MvcContextConfig.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * 定义文件上传的处理器
     *
     * @return
     */
    @Bean("multipartResolver")
    public CommonsMultipartResolver commonsMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10 * 1024 * 1024);
        return multipartResolver;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureMessageConverters(java.util.List)
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringConverter.setWriteAcceptCharset(false);
        converters.add(stringConverter);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new MappingJackson2XmlHttpMessageConverter());
    }

    /* (non-Javadoc) 配置内容协商机制,比如@ResponseBody注解返回的是什么类型的数据,是json还是xml还是String等等
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureContentNegotiation(org.springframework.web.servlet.config
     * .annotation.ContentNegotiationConfigurer)
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.useJaf(false).favorPathExtension(false).favorParameter(true).parameterName("mediaType").ignoreAcceptHeader(true).defaultContentType(MediaType.APPLICATION_JSON);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureDefaultServletHandling(org.springframework.web.servlet.config
     * .annotation.DefaultServletHandlerConfigurer)
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addCorsMappings(org.springframework.web.servlet.config.annotation
     * .CorsRegistry)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("GET", "POST", "DELETE", "PUT").maxAge(3600);

    }

    /* (non-Javadoc)  添加拦截器,推荐使用MappedInterceptor
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addInterceptors(org.springframework.web.servlet.config.annotation
     * .InterceptorRegistry)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor intcep = new LoginInterceptor();
        registry.addInterceptor(new MappedInterceptor(intcep.getIncludePatterns(), intcep.getExcludePatterns(), intcep));
    }

    /* (non-Javadoc) 将数据校验添加到容器中(还未完成)
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#getValidator()
     */
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(HibernateValidator.class);
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasenames("classpath:com/bob/validation/ValidationMessages");
        validator.setValidationMessageSource(messageResource);
        return validator;
    }

    /* (non-Javadoc) 将自定义异常处理器添加到容器中
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureHandlerExceptionResolvers(java.util.List)
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new CustomizedExceptionResolver());
    }

    /* (non-Javadoc) 将自定义的Formatter格式化器添加到容器中
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addFormatters(org.springframework.format.FormatterRegistry)
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new StudentFormatter());
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureAsyncSupport(org.springframework.web.servlet.config.annotation
     * .AsyncSupportConfigurer)
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(5 * 1000);
        configurer.setTaskExecutor(threadPoolTaskExecutor);
        configurer.registerCallableInterceptors(new AsyncCallableInterceptor());
        configurer.registerDeferredResultInterceptors(new AsyncDeferredResultInterceptor());
    }



}
