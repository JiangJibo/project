package com.bob.config.mvc.filter;

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

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpHeaders;

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
        openApiKey = filterConfig.getInitParameter("OPEN_API_KEY");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        if (!request.getRequestURI().endsWith(REQUEST_PERMIT_GENERATING_URI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String referer = request.getHeader("Referer");
        String requestBody = getParamsInString(request);
        PermitResult permit = new PermitResult();
        permit.setToken(generateMD5(openApiKey + "," + referer + "," + requestBody));
        permit.setTimestamp(System.currentTimeMillis());
        //设置返回的内容格式及编码,能够解决中文乱码问题
        servletResponse.setContentType("application/json; charset=utf-8");
        servletResponse.getOutputStream().write(new Gson().toJson(permit).getBytes("UTF-8"));
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
    public String generateMD5(String password) {
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
