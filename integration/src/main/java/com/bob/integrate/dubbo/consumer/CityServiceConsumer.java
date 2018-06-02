package com.bob.integrate.dubbo.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.filter.ConsumerContextFilter;
import com.alibaba.dubbo.rpc.service.GenericService;

import com.bob.integrate.dubbo.common.entity.City;
import com.bob.integrate.dubbo.common.service.CityDubboService;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * 城市 Dubbo 服务消费者
 */
@Component
public class CityServiceConsumer {

    /**
     * 只有 group，interface，version 是服务的匹配条件，三者决定是不是同一个服务，其它配置项均为调优和治理参数
     */
    @Reference(version = "1.0.0", filter = "logging", group = "*", stub = "com.bob.integrate.dubbo.consumer.CityDubboServiceStub")
    private CityDubboService cityDubboService;

    /**
     * {@link ConsumerContextFilter}
     */
    public void printCity() {
        //showContectDetail();
        addAttachment();
        String cityName = "杭州";
        City city = cityDubboService.findCityByName(cityName);
        System.out.println(new Gson().toJson(city));
    }

    /**
     * {@link ConsumerContextFilter#invoke(Invoker, Invocation)}
     */
    private void showContectDetail() {
        String methodName = RpcContext.getContext().getUrl().getAddress();
        System.out.println("调用方法名称：" + methodName);
    }

    private void addAttachment() {
        RpcContext.getContext().setAttachment("attach", "测试Attach");
    }

}
