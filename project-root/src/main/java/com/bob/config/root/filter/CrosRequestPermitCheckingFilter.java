package com.bob.config.root.filter;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.StringUtils;

/**
 * 请求许可验证过滤器
 *
 * @author wb-jjb318191
 * @create 2017-12-08 11:26
 */
public class CrosRequestPermitCheckingFilter implements Filter {

    private static final List<String> EXPOSED_REQUEST_URI_LIST = Arrays.asList();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String path = request.getRequestURI();
        if (!EXPOSED_REQUEST_URI_LIST.contains(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        Map<String, String[]> paramMap = new LinkedHashMap<String, String[]>(request.getParameterMap());
        String[] timestamps = paramMap.remove("timestamp");
        if (timestamps == null | timestamps[0] == null) {
            writeResult(servletResponse, "跨域请求未指定时间戳");
            return;
        }
        String tmstpStr = timestamps[0];
        if (!isNumber(tmstpStr)) {
            writeResult(servletResponse, "时间戳的值不是一个可解析数字");
            return;
        }
        if (Long.valueOf(tmstpStr) < System.currentTimeMillis()) {
            writeResult(servletResponse, "跨域请求许可已过期");
            return;
        }
        String[] tokens = paramMap.remove("token");
        if (tokens == null && !paramMap.isEmpty()) {
            writeResult(servletResponse, "跨域请求存在参数而不存在token");
            return;
        }
        if (tokens != null && paramMap.isEmpty()) {
            writeResult(servletResponse, "跨域请求存在token而不存在参数");
            return;
        }
        //请求无需参数
        if (tokens == null && paramMap.isEmpty()) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        //token和参数不匹配
        if (!verify(new Gson().toJson(paramMap), tokens[0])) {
            writeResult(servletResponse, "跨域请求token和参数不匹配");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
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
