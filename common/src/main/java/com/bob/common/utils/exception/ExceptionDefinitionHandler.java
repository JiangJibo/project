package com.bob.common.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link ExceptionDefinition} 类型的扫描加处理
 *
 * @author wb-jjb318191
 * @create 2019-09-03 10:56
 */
@Configuration
public class ExceptionDefinitionHandler extends ClassPathScanningCandidateComponentProvider
    implements BeanFactoryPostProcessor, InitializingBean, ApplicationContextAware, ImportAware, EnvironmentAware {

    private static final String MESSAGE_FIELD_NAME = "detailMessage";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionDefinitionHandler.class);

    /**
     * 枚举类型 >> 异常类型的映射关系
     */
    private static final LinkedHashMap<Class<? extends ExceptionDefinition>, Class<? extends DefaultServiceException>>
        EXCEPTION_DEFINITION_MAPPINGS = new LinkedHashMap<>();

    private static final Map<String, Field> EXCEPTION_FIELD_MAPPINGS = new HashMap<>();

    private ApplicationContext applicationContext;

    /**
     * 扫描的包路径
     */
    private Set<String> scanPackages = new HashSet<>();

    @Override
    public void setEnvironment(Environment environment) {
        super.setEnvironment(environment);
    }

    /**
     * @param importMetadata
     */
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableYXDAssert.class.getName());
        if (map == null) {
            LOGGER.info("{} was not imported by annotation {}.", ExceptionDefinitionHandler.class.getSimpleName(),
                EnableYXDAssert.class.getSimpleName());
            return;
        }
        String[] scanPackages = AnnotationAttributes.fromMap(map).getStringArray("scanPackages");
        // 如果用注解指定了扫描包
        if (scanPackages.length > 0) {
            for (String pkg : scanPackages) {
                this.scanPackages.add(pkg);
            }
        }
        // 未指定时用启动类的数据
        else {
            try {
                Class mainClass = Class.forName(importMetadata.getClassName());
                initScanPackagesFromMainClass(mainClass);
            } catch (ClassNotFoundException e) {

            }
        }
    }

    /**
     * 获取 {Application} 上 {SpringBootApplication} 的注解指定的扫描包路径，
     * 若不存在用 {Application} 的包路径替代
     */
    @Override
    public void afterPropertiesSet() {
        if (!scanPackages.isEmpty()) {
            LOGGER.info("Imported by annotation {}, has extract scanning packages, dont do it again.",
                EnableYXDAssert.class.getSimpleName());
            return;
        }
        Object application;
        try {
            application = applicationContext.getBean("application");
        } catch (Exception e) {
            LOGGER.warn("No spring boot starter class [Application].");
            return;
        }
        Class clazz = application.getClass();
        if (ClassUtils.isCglibProxy(application)) {
            clazz = ClassUtils.getUserClass(application);
        }
        initScanPackagesFromMainClass(clazz);
    }

    /**
     * 从启动类上收集扫描包数据
     *
     * @param mainClass
     */
    private void initScanPackagesFromMainClass(Class mainClass) {
        Class aClass;
        try {
            aClass = Class.forName("org.springframework.boot.autoconfigure.SpringBootApplication");
        } catch (ClassNotFoundException e) {
            LOGGER.error("SpringBootApplication annotation was not exists.");
            return;
        }
        if (!mainClass.isAnnotationPresent(aClass)) {
            LOGGER.error("Spring boot starter: [*Application] was not annotated by [SpringBootApplication].");
            return;
        }
        Set<String> packages = collectScanPackages(mainClass.getAnnotation(aClass));
        // 如果未指定包,则使用Application的包作为扫描路径
        if (packages.isEmpty()) {
            packages.add(mainClass.getPackage().getName());
        }
        this.scanPackages = packages;
    }

    /**
     * 收集要扫描的类
     *
     * @param annotation
     * @return
     */
    private Set<String> collectScanPackages(Annotation annotation) {
        Set<String> packages = new HashSet<>();
        // 扫描包
        Method packageMethod = ReflectionUtils.findMethod(annotation.getClass(), "scanBasePackages");
        packageMethod.setAccessible(true);
        String[] scanBasePackages = (String[])ReflectionUtils.invokeMethod(packageMethod, annotation);
        Arrays.stream(scanBasePackages).forEach(pkg -> packages.add(pkg));
        // 扫描基类
        Method packageClassesMethod = ReflectionUtils.findMethod(annotation.getClass(), "scanBasePackageClasses");
        packageMethod.setAccessible(true);
        Class<?>[] scanBaseClass = (Class<?>[])ReflectionUtils.invokeMethod(packageClassesMethod, annotation);
        Arrays.stream(scanBaseClass).forEach(clazz -> packages.add(clazz.getPackage().getName()));
        return packages;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        initExceptionMappings();
    }

    /**
     * 根据异常信息实例化一个异常对象,默认用优先级最高的异常类型
     *
     * @param errorMsg
     * @return
     */
    public static DefaultServiceException newException(String errorMsg) {
        checkExceptionExists();
        return newExceptionInternal(EXCEPTION_DEFINITION_MAPPINGS.entrySet().iterator().next().getValue(), null, errorMsg);
    }

    /**
     * 实例化指定类型的异常
     *
     * @param exceptionDefinition
     * @param errorMsg
     * @return
     */
    public static DefaultServiceException newException(ExceptionDefinition exceptionDefinition, Object errorCode, String errorMsg) {
        checkExceptionExists();
        return newExceptionInternal(EXCEPTION_DEFINITION_MAPPINGS.get(exceptionDefinition), errorCode, errorMsg);
    }

    private static DefaultServiceException newExceptionInternal(Class<? extends DefaultServiceException> clazz, Object errorCode, String errorMsg) {
        Field field = EXCEPTION_FIELD_MAPPINGS.get(MESSAGE_FIELD_NAME);
        if (field == null) {
            field = ReflectionUtils.findField(clazz, MESSAGE_FIELD_NAME);
            field.setAccessible(true);
            EXCEPTION_FIELD_MAPPINGS.putIfAbsent(MESSAGE_FIELD_NAME, field);
        }
        DefaultServiceException exception = BeanUtils.instantiateClass(clazz, DefaultServiceException.class);
        exception.setErrorCode(errorCode);
        ReflectionUtils.setField(field, exception, errorMsg);
        return exception;
    }

    /**
     * 初始化 {@link #EXCEPTION_DEFINITION_MAPPINGS}
     * 扫描指定包下所有的{@link ExceptionDefinition}的枚举类型的异常实现
     *
     * @return
     */
    private void initExceptionMappings() {
        this.scanPackages.forEach(s -> scanExceptionDefinitions(s));
        // 兜底逻辑
        EXCEPTION_DEFINITION_MAPPINGS.put(ExceptionDefinition.class, DefaultServiceException.class);
    }

    /**
     * 扫描所有实现ExceptionDefinition的枚举
     *
     * @param basePackage
     * @return
     */
    private void scanExceptionDefinitions(String basePackage) {
        Set<BeanDefinition> definitions = this.findCandidateComponents(basePackage);
        List<Class> classes = definitions.stream().map(beanDefinition -> {
            try {
                return Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        // 异常排序, 按照@Order的value有小到大排序,目的是当未指定异常枚举时, 优先用哪个异常类型
        AnnotationAwareOrderComparator.sort(classes);
        // 将扫描到的枚举及其异常类型加入映射中
        for (Class clazz : classes) {
            EXCEPTION_DEFINITION_MAPPINGS.put(clazz, (Class<? extends DefaultServiceException>)ResolvableType
                .forClass(ExceptionDefinition.class, clazz).getGeneric(0).resolve());
        }
    }

    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) {
        try {
            Class clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
            return clazz.isEnum() && ExceptionDefinition.class.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isIndependent() && metadata.isConcrete() && !metadata.hasEnclosingClass();
    }

    /**
     * 必须存在{@link ExceptionDefinition}的实现类
     */
    private static void checkExceptionExists() {
        Assert.notEmpty(EXCEPTION_DEFINITION_MAPPINGS, "There is no any ExceptionDefinition implementation");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
