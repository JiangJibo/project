package com.bob.config.root.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/**
 * 请求许可验证过滤器
 * [token]和[timestamp]由HttpHeader传入
 *
 * @author wb-jjb318191
 * @create 2017-12-08 11:26
 */
public class CrosRequestPermitCheckingFilter implements Filter {

    private static final List<String> EXPOSED_REQUEST_URI_LIST = Arrays.asList("/adminmap/openapi");
    private static final Integer PERMIT_VALIDITY_IN_MINUTE = 5;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = new CustomizeHttpServletRequestWrapper((HttpServletRequest)servletRequest);
        String path = request.getRequestURI();
        if (!EXPOSED_REQUEST_URI_LIST.contains(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String timestamp = request.getHeader("timestamp");
        if (StringUtils.isEmpty(timestamp)) {
            writeResult(servletResponse, "跨域请求未指定[timestamp]");
            return;
        }
        if (!isNumber(timestamp)) {
            writeResult(servletResponse, "[timestamp]不是一个有效的时间戳");
            return;
        }
        if (Long.valueOf(timestamp) < System.currentTimeMillis() - 1000 * 60 * PERMIT_VALIDITY_IN_MINUTE) {
            writeResult(servletResponse, "跨域请求许可已过期");
            return;
        }
        String requestBody = getRequestBodyInString(request);
        String referer = request.getHeader("Referer");
        //token和参数不匹配
        if (!verify(referer + "," + requestBody, request.getHeader("token"))) {
            writeResult(servletResponse, "跨域请求[token]和参数不匹配");
            return;
        }
        filterChain.doFilter(request, servletResponse);
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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
