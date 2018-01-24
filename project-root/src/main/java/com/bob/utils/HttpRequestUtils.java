package com.bob.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Http请求工具类
 *
 * @author wb-jjb318191
 * @create 2018-01-24 16:55
 */
@Component
public class HttpRequestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUtils.class);

    public static final String GET_REQUEST = "GET";
    public static final String POST_REQUEST = "POST";
    public static final String PUT_REQUEST = "PUT";
    public static final String DELETE_REQUEST = "DELETE";

    public static final String CONNECT_FAILED_RESULT = "尝试连接时出现异常，请重新连接";

    public static final String HTTP_PROTOCOL = "http://";

    public static final String HTTP_PORT = ":8080";

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static final Map<String, String> HEADER_CACHE = new HashMap<String, String>(16);

    /**
     * 根据IP地址组装成完整的请求路径
     *
     * @param hostAdress
     * @return
     */
    public static String createRequestUrl(String hostAdress, String webRoot, String controllerPath) {
        return HttpRequestUtils.HTTP_PROTOCOL + hostAdress + HttpRequestUtils.HTTP_PORT + webRoot + controllerPath;
    }

    /**
     * 获取URL上的主机IP
     *
     * @param requestUrl
     * @return
     */
    public static String getHostAdress(String requestUrl) {
        return requestUrl.subSequence(requestUrl.indexOf("//") + 2, requestUrl.lastIndexOf(":")).toString();
    }

    /**
     * 在当前线程内发起Delete请求
     *
     * @param url     请求URL
     * @param timeout 请求时限,单位毫秒,0及以下代表不限时
     * @return
     */
    public static String doGet(String url, int timeout) {
        return doRequest(url, GET_REQUEST, null, timeout);
    }

    /**
     * 在当前线程内发起Delete请求
     *
     * @param url     请求URL
     * @param timeout 请求时限,单位毫秒,0及以下代表不限时
     * @return
     * @throws Exception
     */
    public static String doDelete(String url, int timeout) {
        return doRequest(url, DELETE_REQUEST, null, timeout);
    }

    /**
     * 在当前线程内发起Post请求
     *
     * @param url         请求URL
     * @param requestBody 请求提交的Entity
     * @param timeout     请求时限,单位毫秒,0及以下代表不限时
     * @return
     * @throws Exception
     */
    public static String doPost(String url, String requestBody, int timeout) {
        return doRequest(url, POST_REQUEST, requestBody, timeout);
    }

    /**
     * 在当前线程内发起Put请求
     *
     * @param url         请求URL
     * @param requestBody 请求提交的Entity
     * @param timeout     请求时限,单位毫秒,0及以下代表不限时
     * @return
     */
    public static String doPut(String url, String requestBody, int timeout) {
        return doRequest(url, PUT_REQUEST, requestBody, timeout);
    }

    /**
     * 在当前线程发起Http请求
     *
     * @param url         请求的URL路径
     * @param method      请求的方式
     * @param requestBody
     * @param timeout     请求的时限
     * @return
     */
    private static String doRequest(String url, String method, String requestBody, int timeout) {
        String line;
        InputStream is = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            conn = openConnection(method, url);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            addHeaderForRequest(conn);
            if (requestBody != null && conn.getDoOutput()) {
                OutputStream os = conn.getOutputStream();
                os.write(requestBody.getBytes());
                os.flush();
                os.close();
            }
            is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logE("以" + method + "方式请求:" + url + " 时出现异常");
            return CONNECT_FAILED_RESULT;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                return CONNECT_FAILED_RESULT;
            }
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    /**
     * 添加header数据
     *
     * @param connection
     */
    private static void addHeaderForRequest(HttpURLConnection connection) {
        for (Entry<String, String> entry : HEADER_CACHE.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 指定Http请求的方式
     *
     * @param method
     * @param path
     * @return
     * @throws IOException
     */
    public static HttpURLConnection openConnection(String method, String path) throws IOException {
        URL url = new URL(path);// 请求的URL地址
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        switch (method) {
            case GET_REQUEST:
                // 禁用网络缓存
                conn.setUseCaches(false);
            case DELETE_REQUEST:
                // HttpURLConnection默认就是用GET发送请求，所以下面的setRequestMethod可以省略
                conn.setRequestMethod(method);
                // HttpURLConnection默认也支持从服务端读取结果流，所以下面的setDoInput也可以省略
                conn.setDoInput(true);
                return conn;
            case POST_REQUEST:
            case PUT_REQUEST:
                conn.setRequestMethod(method);
                // 调用conn.setDoOutput()方法以显式开启请求体
                conn.setDoOutput(true);
                return conn;
            default:
                throw new IllegalStateException("非法请求方式");
        }
    }

    private static void logE(String msg) {
        LOGGER.error("请求:[{}]时发生异常", msg);
    }

    /**
     * 給Request的Header添加数据
     *
     * @param name
     * @param value
     * @return false:内部原有的header被覆盖; true:新增的header
     */
    public static boolean cacheHeader(String name, String value) {
        return HEADER_CACHE.put(name, value) != null;
    }

    /**
     * 是否存在指定的header属性
     *
     * @param name
     * @return
     */
    public static boolean conttainHeaderAttribute(String name) {
        return HEADER_CACHE.containsKey(name);
    }

    /**
     * 删除Header数据
     *
     * @param name
     * @return false:不存在指定的header;true:删除成功
     */
    public static boolean removeHeader(String name) {
        return HEADER_CACHE.remove(name) != null;
    }

    /**
     * 清空header缓存
     */
    public static void clearHeaderCache() {
        HEADER_CACHE.clear();
    }

    // 读取请求头
    @SuppressWarnings("unused")
    private String getReqeustHeader(HttpURLConnection conn) {
        // https://github.com/square/okhttp/blob/master/okhttp-urlconnection/src/main/java/okhttp3/internal/huc/HttpURLConnectionImpl.java#L236
        Map<String, List<String>> requestHeaderMap = conn.getRequestProperties();
        Iterator<String> requestHeaderIterator = requestHeaderMap.keySet().iterator();
        StringBuilder sbRequestHeader = new StringBuilder();
        while (requestHeaderIterator.hasNext()) {
            String requestHeaderKey = requestHeaderIterator.next();
            String requestHeaderValue = conn.getRequestProperty(requestHeaderKey);
            sbRequestHeader.append(requestHeaderKey);
            sbRequestHeader.append(":");
            sbRequestHeader.append(requestHeaderValue);
            sbRequestHeader.append("\n");
        }
        return sbRequestHeader.toString();
    }

    // 读取响应头
    private static String getResponseHeader(HttpURLConnection conn) {
        Map<String, List<String>> responseHeaderMap = conn.getHeaderFields();
        int size = responseHeaderMap.size();
        StringBuilder sbResponseHeader = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String responseHeaderKey = conn.getHeaderFieldKey(i);
            String responseHeaderValue = conn.getHeaderField(i);
            sbResponseHeader.append(responseHeaderKey);
            sbResponseHeader.append(":");
            sbResponseHeader.append(responseHeaderValue);
            sbResponseHeader.append("\n");
        }
        return sbResponseHeader.toString();
    }

}
