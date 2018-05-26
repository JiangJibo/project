package com.bob.integrate.dubbo.consumer.extension;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.InvokerListener;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * @author Administrator
 * @create 2018-05-26 17:56
 */
public class InvokerLoggingListener implements InvokerListener {

    @Override
    public void referred(Invoker<?> invoker) throws RpcException {
    }

    @Override
    public void destroyed(Invoker<?> invoker) {

    }
}
