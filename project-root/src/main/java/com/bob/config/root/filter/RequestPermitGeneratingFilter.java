package com.bob.config.root.filter;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;

/**
 * 请求许可生成过滤器
 *
 * @author wb-jjb318191
 * @create 2017-12-08 10:40
 */
public class RequestPermitGeneratingFilter implements Filter {

    private static final String REQUEST_PERMIT_GENERATING_URI = "/adminmap/api";
    private static final Integer PERMIT_VALIDITY_IN_MINUTE = 1;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        if (!request.getRequestURI().endsWith(REQUEST_PERMIT_GENERATING_URI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        StringBuilder sb = new StringBuilder();
        Map<String, String[]> paramMap = request.getParameterMap();
        if (!paramMap.isEmpty()) {
            sb.append("token=" + generateMD5(new Gson().toJson(paramMap))).append("&");
        }
        sb.append("timestamp=" + (System.currentTimeMillis() + 1000 * 60 * PERMIT_VALIDITY_IN_MINUTE));
        servletResponse.getOutputStream().write(sb.toString().getBytes("UTF-8"));
    }

    /**
     * 加盐MD5
     *
     * @param password
     * @return
     */
    private String generateMD5(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
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
