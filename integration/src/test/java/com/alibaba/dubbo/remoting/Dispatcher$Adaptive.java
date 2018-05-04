package com.alibaba.dubbo.remoting;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

/**
 * @author Administrator
 * @create 2018-04-25 22:56
 */
public class Dispatcher$Adaptive implements Dispatcher {

    public com.alibaba.dubbo.remoting.ChannelHandler dispatch(ChannelHandler arg0, URL arg1) {
        if (arg1 == null) {
            throw new IllegalArgumentException("url == null");
        }
        URL url = arg1;
        String extName = url.getParameter("dispatcher", url.getParameter("dispather", url.getParameter("channel.handler", "all")));
        if (extName == null) {
            throw new IllegalStateException("Fail to get extension(com.alibaba.com.alibaba.dubbo.remoting.Dispatcher) name from url(" + url.toString()
                + ") use keys([dispatcher, dispather, channel.handler])");
        }
        Dispatcher extension = ExtensionLoader.getExtensionLoader(Dispatcher.class).getExtension(extName);
        return extension.dispatch(arg0, arg1);
    }
}
