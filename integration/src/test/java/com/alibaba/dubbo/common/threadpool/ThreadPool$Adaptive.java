package com.alibaba.dubbo.common.threadpool;

import java.util.concurrent.Executor;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

/**
 * @author Administrator
 * @create 2018-04-25 22:58
 */
public class ThreadPool$Adaptive implements ThreadPool {

    public Executor getExecutor(URL arg0) {
        if (arg0 == null) {
            throw new IllegalArgumentException("url == null");
        }
        URL url = arg0;
        String extName = url.getParameter("threadpool", "fixed");
        if (extName == null) {
            throw new IllegalStateException(
                "Fail to get extension(com.alibaba.com.alibaba.dubbo.common.threadpool.ThreadPool) name from url(" + url.toString() + ") use keys([threadpool])");
        }
        ThreadPool extension = ExtensionLoader.getExtensionLoader(ThreadPool.class).getExtension(extName);
        return extension.getExecutor(arg0);
    }
}
