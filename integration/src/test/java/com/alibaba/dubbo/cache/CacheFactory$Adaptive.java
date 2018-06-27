package com.alibaba.dubbo.cache;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Invocation;

/**
 * javasistent动态代理生成的CacheFactory
 *
 * @author Administrator
 * @create 2018-04-25 22:41
 */
public class CacheFactory$Adaptive implements CacheFactory {

    public Cache getCache(URL arg0, Invocation invocation) {
        if (arg0 == null) {
            throw new IllegalArgumentException("url == null");
        }
        URL url = arg0;
        String extName = url.getParameter("cache", "lru");
        if (extName == null) {
            throw new IllegalStateException(
                "Fail to get extension(com.alibaba.dubbo.cache.CacheFactory) name from url(" + url.toString() + ") use keys([cache])");
        }
        CacheFactory extension = ExtensionLoader.getExtensionLoader(CacheFactory.class).getExtension(extName);
        return extension.getCache(arg0, invocation);
    }
}
