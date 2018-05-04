package com.alibaba.dubbo.remoting.zookeeper;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

/**
 * @author Administrator
 * @create 2018-04-25 22:59
 */
public class ZookeeperTransporter$Adaptive implements ZookeeperTransporter {

    public ZookeeperClient connect(URL arg0) {
        if (arg0 == null) {
            throw new IllegalArgumentException("url == null");
        }
        URL url = arg0;
        String extName = url.getParameter("client", url.getParameter("transporter", "zkclient"));
        if (extName == null) {
            throw new IllegalStateException("Fail to get extension(com.alibaba.com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter) name from url(" + url.toString()
                + ") use keys([client, transporter])");
        }
        ZookeeperTransporter extension = ExtensionLoader.getExtensionLoader(ZookeeperTransporter.class).getExtension(extName);
        return extension.connect(arg0);
    }
}
