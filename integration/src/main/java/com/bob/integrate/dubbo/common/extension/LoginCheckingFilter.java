package com.bob.integrate.dubbo.common.extension;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * @create 2018-05-26 17:40
 */
public class LoginCheckingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginCheckingFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        LOGGER.debug("执行{}方法,参数为{}", invocation.getMethodName(), invocation.getArguments());
        URL url = invoker.getUrl();
        String userName = url.getUsername();
        String password = url.getPassword();
        LOGGER.info("userName:[{}],password:[{}]", userName, password);
        return invoker.invoke(invocation);
    }
}
