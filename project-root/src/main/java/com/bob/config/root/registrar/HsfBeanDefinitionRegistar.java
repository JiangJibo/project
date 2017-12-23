package com.bob.config.root.registrar;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

/**
 * Hsf注册器
 *
 * @author Administrator
 * @create 2017-12-22 23:16
 */
public class HsfBeanDefinitionRegistar implements ImportBeanDefinitionRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(HsfBeanDefinitionRegistar.class);

    private static final String RESOURCE_PATTERN = "**/*.class";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annAttr = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(HsfComponentScan.class.getName()));
        String[] basePackages = annAttr.getStringArray("basePackages");
        if (basePackages == null) {
            basePackages = getPackagesFromClasses(annAttr.getClassArray("basePackageClasses"));
        }
        if (basePackages == null) {
            basePackages = new String[] {ClassUtils.getPackageName(importingClassMetadata.getClassName())};
        }
        List<TypeFilter> includeFilters = extractTypeFilters(annAttr.getAnnotationArray("includeFilters"));
        List<TypeFilter> excludeFilters = extractTypeFilters(annAttr.getAnnotationArray("excludeFilters"));
        List<String> candidateBeanNames = new ArrayList<String>();
        for (String pkg : basePackages) {
            try {
                candidateBeanNames.addAll(findCandidateClassNames(pkg, includeFilters, excludeFilters));
            } catch (IOException e) {
                LOGGER.error("扫描指定HSF基础包%s时出现异常", pkg);
                continue;
            }
        }
        if (candidateBeanNames.isEmpty()) {
            LOGGER.info("扫描指定HSF基础包%s时未发现复合条件的基础类", basePackages.toString());
        }
        List<Class<?>> internalClasses = transformToClass(candidateBeanNames);
        //是否要注册HSF内部的指定Bean,注册SERVICE
        if (annAttr.getBoolean("registerInternalBeans")) {
            registerInternalBeanDefinitions(internalClasses, registry);
        }
        //注册HSF
        registerHsfBeanDefinitions(internalClasses, registry);
    }

    /**
     * 获取符合要求的Controller名称
     *
     * @param basePackage
     * @return
     * @throws IOException
     */
    private List<String> findCandidateClassNames(String basePackage, List<TypeFilter> includeFilters, List<TypeFilter> excludeFilters) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始扫描包[" + basePackage + "]下的所有类");
        }
        List<String> controllers = new ArrayList<String>();
        String packageSearchPath = CLASSPATH_ALL_URL_PREFIX + replaceDotByDelimiter(basePackage) + '/' + RESOURCE_PATTERN;
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(resourceLoader);
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(packageSearchPath);
        for (Resource resource : resources) {
            MetadataReader reader = readerFactory.getMetadataReader(resource);
            if (isCandidateController(reader, readerFactory, includeFilters, excludeFilters)) {
                controllers.add(reader.getClassMetadata().getClassName());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("扫描到符合要求开放Controller类:[" + controllers.get(controllers.size() - 1) + "]");
                }
            }
        }
        return controllers;
    }

    /**
     * @param internalClasses
     * @param registry
     */
    private void registerInternalBeanDefinitions(List<Class<?>> internalClasses, BeanDefinitionRegistry registry) {
        for (Class<?> clazz : internalClasses) {
            RootBeanDefinition rbd = new RootBeanDefinition(clazz);
            registry.registerBeanDefinition(ClassUtils.getShortNameAsProperty(clazz), rbd);
        }
    }

    /**
     * @param internalClasses
     * @param registry
     */
    private void registerHsfBeanDefinitions(List<Class<?>> internalClasses, BeanDefinitionRegistry registry) {
        for (Class<?> clazz : internalClasses) {
            RootBeanDefinition rbd = new RootBeanDefinition((Class<?>)null);
            rbd.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
            MutablePropertyValues mpv = new MutablePropertyValues();
        }
    }

    /**
     * @param filterAttributes
     * @return
     */
    private List<TypeFilter> typeFiltersFor(AnnotationAttributes filterAttributes) {
        List<TypeFilter> typeFilters = new ArrayList<TypeFilter>();
        FilterType filterType = filterAttributes.getEnum("type");

        for (Class<?> filterClass : filterAttributes.getClassArray("classes")) {
            switch (filterType) {
                case ANNOTATION:
                    Assert.isAssignable(Annotation.class, filterClass,
                        "@ComponentScan ANNOTATION type filter requires an annotation type");
                    @SuppressWarnings("unchecked")
                    Class<Annotation> annotationType = (Class<Annotation>)filterClass;
                    typeFilters.add(new AnnotationTypeFilter(annotationType));
                    break;
                case ASSIGNABLE_TYPE:
                    typeFilters.add(new AssignableTypeFilter(filterClass));
                    break;
                case CUSTOM:
                    Assert.isAssignable(TypeFilter.class, filterClass,
                        "@ComponentScan CUSTOM type filter requires a TypeFilter implementation");
                    TypeFilter filter = BeanUtils.instantiateClass(filterClass, TypeFilter.class);
                    typeFilters.add(filter);
                    break;
                default:
                    throw new IllegalArgumentException("Filter type not supported with Class value: " + filterType);
            }
        }

        for (String expression : filterAttributes.getStringArray("pattern")) {
            switch (filterType) {
                case REGEX:
                    typeFilters.add(new RegexPatternTypeFilter(Pattern.compile(expression)));
                    break;
                case ASPECTJ:
                    throw new UnsupportedOperationException("不支持切面式的@Filter定义");
                default:
                    throw new IllegalArgumentException("Filter type not supported with String pattern: " + filterType);
            }
        }

        return typeFilters;
    }

    /**
     * @param classes
     * @return
     */
    private String[] getPackagesFromClasses(Class[] classes) {
        if (ObjectUtils.isEmpty(classes)) {
            return null;
        }
        List<String> basePackages = new ArrayList<String>(classes.length);
        for (Class<?> clazz : classes) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        return (String[])basePackages.toArray();
    }

    /**
     * 将类名转换为类对象
     *
     * @param classNames
     * @return
     */
    private List<Class<?>> transformToClass(List<String> classNames) {
        List<Class<?>> classes = new ArrayList<Class<?>>(classNames.size());
        for (String className : classNames) {
            try {
                classes.add(ClassUtils.forName(className, this.getClass().getClassLoader()));
            } catch (ClassNotFoundException e) {
                LOGGER.info("未找到指定HSF基础类[%s]", className);
                continue;
            }
        }
        return classes;
    }

    /**
     * 用"/"替换包路径中"."
     *
     * @param path
     * @return
     */
    private String replaceDotByDelimiter(String path) {
        return StringUtils.replace(path, ".", "/");
    }

    /**
     * @param reader
     * @param readerFactory
     * @param includeFilters
     * @param excludeFilters
     * @return
     * @throws IOException
     */
    protected boolean isCandidateController(MetadataReader reader, MetadataReaderFactory readerFactory, List<TypeFilter> includeFilters,
                                            List<TypeFilter> excludeFilters) throws IOException {
        for (TypeFilter tf : excludeFilters) {
            if (tf.match(reader, readerFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : includeFilters) {
            if (tf.match(reader, readerFactory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param annAttrs
     * @return
     */
    private List<TypeFilter> extractTypeFilters(AnnotationAttributes[] annAttrs) {
        List<TypeFilter> typeFilters = new ArrayList<TypeFilter>();
        for (AnnotationAttributes filter : annAttrs) {
            typeFilters.addAll(typeFiltersFor(filter));
        }
        return typeFilters;
    }

}
