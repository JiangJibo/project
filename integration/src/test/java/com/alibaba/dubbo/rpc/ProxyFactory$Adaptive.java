package com.alibaba.dubbo.rpc;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

/**
 * Javasistent生成的ProxyFactory的动态代理类
 *
 * @author Administrator
 * @create 2018-04-25 22:33
 */
public class ProxyFactory$Adaptive implements ProxyFactory {

    public Invoker getInvoker(Object arg0, Class arg1, URL arg2) {
        if (arg2 == null) {
            throw new IllegalArgumentException("url == null");
        }
        URL url = arg2;
        String extName = url.getParameter("proxy", "javassist");
        if (extName == null) {
            throw new IllegalStateException(
                "Fail to get extension(com.alibaba.dubbo.rpc.ProxyFactory) name from url(" + url.toString() + ") use keys([proxy])");
        }
        ProxyFactory extension = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(extName);
        return extension.getInvoker(arg0, arg1, arg2);
    }

    public Object getProxy(Invoker arg0) {
        if (arg0 == null) {
            throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument == null");
        }
        if (arg0.getUrl() == null) {
            throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument getUrl() == null");
        }
        URL url = arg0.getUrl();
        String extName = url.getParameter("proxy", "javassist");
        if (extName == null) {
            throw new IllegalStateException(
                "Fail to get extension(com.alibaba.dubbo.rpc.ProxyFactory) name from url(" + url.toString() + ") use keys([proxy])");
        }
        ProxyFactory extension = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(extName);
        return extension.getProxy(arg0);
    }

}
