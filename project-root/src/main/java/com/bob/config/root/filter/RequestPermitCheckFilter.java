package com.bob.config.root.filter;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;

/**
 * 请求许可验证过滤器
 *
 * @author wb-jjb318191
 * @create 2017-12-08 11:26
 */
public class RequestPermitCheckFilter implements Filter {

    private static final List<String> EXCLUDE_REQUEST_PATH_LIST = Arrays.asList();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String path = request.getRequestURI();
        if(EXCLUDE_REQUEST_PATH_LIST.contains(path)){

        }
    }

    /**
     * 校验加盐后是否和原文一致
     *
     * @param password
     * @param md5
     * @return
     */
    public boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
