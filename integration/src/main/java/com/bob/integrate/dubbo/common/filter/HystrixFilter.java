package com.bob.integrate.dubbo.common.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * Hystrix过滤器
 *
 * @author wb-jjb318191
 * @create 2018-08-15 14:40
 */
@Activate(group = Constants.CONSUMER)
public class HystrixFilter implements Filter {

    @Override
    public Result invoke(Invoker invoker, Invocation invocation) throws RpcException {
        return new DubboHystrixCommand(invoker, invocation).execute();
    }
}
