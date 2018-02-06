package com.bob.project.config;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;

import com.bob.project.config.async.AsyncCallableInterceptor;
import com.bob.project.config.async.AsyncDeferredResultInterceptor;
import com.bob.project.config.exception.CustomizedExceptionResolver;
import com.bob.project.config.filter.SpringBeanInstanceAccessor;
import com.bob.project.config.formatter.String2DateFormatter;
import com.bob.project.config.formatter.StudentFormatter;
import com.bob.project.config.interceptor.LoginInterceptor;
import com.bob.project.config.scanfilter.MvcContextScanExcludeFilter;
import com.bob.project.config.userenv.AppUserContextConfig;
import com.bob.project.utils.validate.EnableDataValidate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.HibernateValidator;
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
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月5日 下午4:20:35
 */
@Configuration
@EnableAsync
@EnableWebMvc
@EnableDataValidate
@ComponentScan(basePackages = {"com.bob.project.mvc"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({AppUserContextConfig.class})
public class MvcContextConfig extends WebMvcConfigurerAdapter {

    final static Logger LOGGER = LoggerFactory.getLogger(MvcContextConfig.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Bean
    public SpringBeanInstanceAccessor customizedBeanFactoryUtils() {
        return new SpringBeanInstanceAccessor();
    }

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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringConverter.setWriteAcceptCharset(false);
        converters.add(stringConverter);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new MappingJackson2XmlHttpMessageConverter());
        //设置Date类型使用HttpMessageConverter转换后的格式,或者注册一个GsonHttpMessageConverter,能直接支持字符串到日期的转换
        //当指定了日期字符串格式后,如果传的日志格式不符合,则会解析错误
        converters.add(new MappingJackson2HttpMessageConverter(
            new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))));
        //GsonHttpMessageConverter不支持yyyy-MM-dd形式的字符串转换为日期
        //converters.add(new GsonHttpMessageConverter());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.useJaf(false).favorPathExtension(false).favorParameter(true).parameterName("mediaType")
            .ignoreAcceptHeader(true).defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //exposedHeaders设置Header可拓展的参数名称,比如希望用将token参数用Header传递,需要设置exposedHeaders("token")
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true)
            .allowedMethods("GET", "POST", "DELETE", "PUT").maxAge(3600).exposedHeaders("");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor intcep = new LoginInterceptor();
        registry.addInterceptor(new MappedInterceptor(intcep.getIncludePatterns(), intcep.getExcludePatterns(), intcep));
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(HibernateValidator.class);
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasenames("classpath:com/bob/validation/ValidationMessages");
        validator.setValidationMessageSource(messageResource);
        return validator;
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new CustomizedExceptionResolver());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new String2DateFormatter());
        registry.addFormatter(new StudentFormatter());
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(5 * 1000);
        configurer.setTaskExecutor(threadPoolTaskExecutor);
        configurer.registerCallableInterceptors(new AsyncCallableInterceptor());
        configurer.registerDeferredResultInterceptors(new AsyncDeferredResultInterceptor());
    }

}
