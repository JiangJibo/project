package com.bob.integrate.dubbo.common.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wb-jjb318191
 * @create 2018-08-15 14:37
 */
public class DubboHystrixCommand extends HystrixCommand<Result> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboHystrixCommand.class);

    private static final int DEFAULT_THREADPOOL_CORE_SIZE = 30;
    private Invoker invoker;
    private Invocation invocation;

    public DubboHystrixCommand(Invoker invoker, Invocation invocation) {
        super(
            Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName())
            )
            .andCommandKey(HystrixCommandKey.Factory.asKey(
                String.format("%s_%d", invocation.getMethodName(),
                invocation.getArguments() == null ? 0 : invocation.getArguments().length))
            )
            .andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter()
                //10秒钟内至少19此请求失败，熔断器才发挥起作用
                .withCircuitBreakerRequestVolumeThreshold(20)
                //熔断器中断请求30秒后会进入半打开状态,放部分流量过去重试
                .withCircuitBreakerSleepWindowInMilliseconds(30000)
                //错误率达到50开启熔断保护
                .withCircuitBreakerErrorThresholdPercentage(50)
                //使用dubbo的超时，禁用这里的超时
                .withExecutionTimeoutEnabled(false)
            )
            .andThreadPoolPropertiesDefaults(
                HystrixThreadPoolProperties.Setter().withCoreSize(getThreadPoolCoreSize(invoker.getUrl()))
            )
        );//线程池为30

        this.invoker = invoker;
        this.invocation = invocation;
    }

    /**
     * 获取线程池大小
     *
     * @param url
     * @return
     */
    private static int getThreadPoolCoreSize(URL url) {
        if (url != null) {
            int size = url.getParameter("ThreadPoolCoreSize", DEFAULT_THREADPOOL_CORE_SIZE);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ThreadPoolCoreSize:" + size);
            }
            return size;
        }

        return DEFAULT_THREADPOOL_CORE_SIZE;

    }

    @Override
    protected Result run() throws Exception {
        return invoker.invoke(invocation);
    }

}
