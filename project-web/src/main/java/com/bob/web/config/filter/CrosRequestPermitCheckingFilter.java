package com.bob.web.config.filter;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.bob.web.mvc.mapper.BankUserMapper;
import com.bob.web.mvc.entity.model.BankUser;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

/**
 * 请求许可验证过滤器
 * [token]和[timestamp]由HttpHeader传入
 *
 * @author wb-jjb318191
 * @create 2017-12-08 11:26
 */
public class CrosRequestPermitCheckingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrosRequestPermitCheckingFilter.class);

    private static final String RESOURCE_PATTERN = "**/*.class";

    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();
    private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();

    private static final String APPLICATION_NAMESPACE = "";
    private static final Integer PERMIT_VALIDITY_IN_MINUTE = 5;
    private static final List<String> EXPOSED_CONTROLLER_PACKAGES = Arrays.asList("com.bob.mvc.controller");

    private Set<String> exposedRequestUriSet = new LinkedHashSet<String>();

    /**
     * 初始化,扫描开放的Controller包,生成开放的URL集合
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        includeFilters.add(new AnnotationTypeFilter(RequestMapping.class, false));
        List<String> controllers = new ArrayList<String>();
        try {
            for (String pkg : EXPOSED_CONTROLLER_PACKAGES) {
                controllers.addAll(this.findCandidateControllers(pkg));
            }
            if (controllers.isEmpty()) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("扫描指定包{}时未发现符合的开放Controller类", EXPOSED_CONTROLLER_PACKAGES.toString());
                }
                return;
            }
            generateExposedURL(this.transformToClass(controllers), APPLICATION_NAMESPACE);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("扫描指定Controller包,发现开放URL:{}", exposedRequestUriSet.toString());
            }
        } catch (Exception e) {
            LOGGER.error("扫描开放Controller出现异常", e);
            return;
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = new HttpRequestTwiceReadingWrapper((HttpServletRequest)servletRequest);
        String path = request.getRequestURI();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (!exposedRequestUriSet.contains(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            processCrosRequestPermitCkecking(request);
        } catch (IllegalArgumentException | IllegalStateException e) {
            writeResult(servletResponse, e.getMessage());
        }
        filterChain.doFilter(request, servletResponse);
    }

    /**
     * 处理跨域请求许可验证
     *
     * @param request
     * @throws Exception
     */
    private void processCrosRequestPermitCkecking(HttpServletRequest request) throws IOException {
        String timestamp = request.getHeader("timestamp");
        Assert.hasText(timestamp, "跨域请求未指定[timestamp]");
        Assert.state(isNumber(timestamp), "[timestamp]不是一个有效的时间戳");
        Assert.state(Long.valueOf(timestamp) >= System.currentTimeMillis() - 1000 * 60 * PERMIT_VALIDITY_IN_MINUTE, "跨域请求许可已过期");
        String referer = request.getHeader("Referer");
        String requestBody = getRequestBodyInString(request);
        String appcode = getTargetProperty(requestBody, "appcode");
        Assert.hasText(appcode, "跨域请求[appcode]不存在");
        String campusId = getTargetProperty(requestBody, "campusId");
        Assert.isTrue(isNumber(campusId), "跨域请求[campusId]不正确");
        String key = selectKey(appcode, campusId);
        Assert.notNull(key, "跨域请求[appcode]和[campusId]相应的key不存在");
        Assert.state(verify(key + "," + referer + "," + requestBody, request.getHeader("token")), "跨域请求[token]和参数不匹配");
    }

    /**
     * 写入错误结果
     *
     * @param servletResponse
     * @param result
     * @throws IOException
     */
    private void writeResult(ServletResponse servletResponse, String result) throws IOException {
        servletResponse.getOutputStream().write(result.getBytes("UTF-8"));
    }

    /**
     * 如果RequestBody内没有数据，则返回""
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String getRequestBodyInString(HttpServletRequest request) throws IOException {
        InputStream is = request.getInputStream();
        byte[] bytes = new byte[2048];
        int length = is.read(bytes);
        return length < 0 ? "" : new String(Arrays.copyOf(bytes, length), "UTF-8");
    }

    /**
     * 从json字符串中获取指定属性的值
     *
     * @param json
     * @param property
     * @return
     */
    private String getTargetProperty(String json, String property) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        String[] fragments = json.split(",");
        String value = null;
        for (String fragment : fragments) {
            if (fragment.contains(property)) {
                value = fragment.substring(fragment.indexOf(":") + 1).trim();
                if (value.contains("}")) {
                    value = value.substring(0, value.indexOf("}")).trim();
                }
                if (value.contains("\"")) {
                    value = value.substring(1, value.length() - 1).trim();
                }
                break;
            }
        }
        return value;
    }

    /**
     * 校验加盐后是否和原文一致
     *
     * @param password
     * @param md5
     * @return
     */
    private boolean verify(String password, String md5) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(md5)) {
            return false;
        }
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        return new String(cs1).equals(md5Hex(password + new String(cs2)));
    }

    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    private String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串是否是数字
     *
     * @param value
     * @return
     */
    private boolean isNumber(String value) {
        char[] chars = ((String)value).toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取符合要求的Controller名称
     *
     * @param basePackage
     * @return
     * @throws IOException
     */
    private List<String> findCandidateControllers(String basePackage) throws IOException {
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
            if (isCandidateController(reader, readerFactory)) {
                controllers.add(reader.getClassMetadata().getClassName());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("扫描到符合要求开放Controller类:[" + controllers.get(controllers.size() - 1) + "]");
                }
            }
        }
        return controllers;
    }

    /**
     * @param reader
     * @param readerFactory
     * @return
     * @throws IOException
     */
    protected boolean isCandidateController(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
        for (TypeFilter tf : this.excludeFilters) {
            if (tf.match(reader, readerFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(reader, readerFactory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将类名转换为类对象
     *
     * @param classNames
     * @return
     * @throws ClassNotFoundException
     */
    private List<Class<?>> transformToClass(List<String> classNames) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>(classNames.size());
        for (String className : classNames) {
            classes.add(ClassUtils.forName(className, this.getClass().getClassLoader()));
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
     * 内省Controllers,生成开放的URL集合
     *
     * @param controllers
     * @param prefix
     */
    private void generateExposedURL(List<Class<?>> controllers, String prefix) {
        for (Class<?> controller : controllers) {
            String[] classMappings = controller.getAnnotation(RequestMapping.class).value();
            ReflectionUtils.doWithMethods(controller,
                (method) -> {
                    String[] methodMappings = method.getAnnotation(RequestMapping.class).value();
                    exposedRequestUriSet.add(prefix + transformMappings(classMappings) + transformMappings(methodMappings));
                },
                (method) -> method.isAnnotationPresent(RequestMapping.class)
            );
        }
    }

    /**
     * TODO
     *
     * @param appcode
     * @param campusId
     * @return
     */
    public String selectKey(String appcode, String campusId) {
        BankUserMapper bankUserMapper = (BankUserMapper)SpringBeanInstanceAccessor.getBean(BankUserMapper.class);
        BankUser bankUser = bankUserMapper.selectByIdAndAge(Integer.valueOf(campusId), Integer.valueOf(appcode));
        if (bankUser == null) {
            return null;
        }
        return bankUser.getIdcard();
    }

    /**
     * 如果方法或者类上的{@linkplain RequestMapping#value()}未指定,则使用""代替
     * value()仅支持单个值
     *
     * @param mappings
     * @return
     */
    private String transformMappings(String[] mappings) {
        return ObjectUtils.isEmpty(mappings) ? "" : mappings[0];
    }

    @Override
    public void destroy() {

    }

}
