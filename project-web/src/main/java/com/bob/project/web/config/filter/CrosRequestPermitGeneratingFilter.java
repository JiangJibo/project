package com.bob.project.web.config.filter;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;

/**
 * 请求许可生成过滤器
 *
 * @author wb-jjb318191
 * @create 2017-12-08 10:40
 */
public class CrosRequestPermitGeneratingFilter implements Filter {

    private String openApiKey;
    private static final String REQUEST_PERMIT_GENERATING_URI = "/adminmap/openapi";
    private static final Gson GSON = new Gson();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //openApiKey = filterConfig.getInitParameter("OPEN_API_KEY");
        openApiKey = "0123456789";
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        if (!request.getRequestURI().endsWith(REQUEST_PERMIT_GENERATING_URI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        configCrosAccess(request, (HttpServletResponse)servletResponse);
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        String referer = request.getHeader("Referer");
        String requestBody = getParamsInString(request);
        PermitResult permit = new PermitResult();
        permit.setToken(generateMD5(openApiKey + "," + referer + "," + requestBody));
        permit.setTimestamp(System.currentTimeMillis());
        System.out.println("请求参数为:" + requestBody);
        System.out.println("生成token为:" + permit.getToken());
        servletResponse.getOutputStream().write(GSON.toJson(permit).getBytes("UTF-8"));
    }

    /**
     * 配置跨域
     *
     * @param httpRequest
     * @param httpResponse
     */
    private void configCrosAccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String origin = httpRequest.getHeader("Origin");
        if (origin == null) {
            httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        } else {
            httpResponse.addHeader("Access-Control-Allow-Origin", origin);
        }
        //httpResponse.addHeader("Content-Encoding", "gzip");
        httpResponse.addHeader("Access-Control-Allow-Headers", "Origin, x-requested-with, Content-Type, Accept, X-Cookie, timestamp, token, debugKey");
        httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,OPTIONS,DELETE");
    }

    /**
     * 以字符串形式获取参数集合
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String getParamsInString(HttpServletRequest request) throws IOException {
        return GSON.toJson(request.getParameterMap());
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
    public void destroy() {

    }

    private static class PermitResult {

        private String token;
        private long timestamp;
        private boolean success = true;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

}
