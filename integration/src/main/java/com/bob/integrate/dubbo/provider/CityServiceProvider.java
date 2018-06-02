package com.bob.integrate.dubbo.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;

import com.bob.integrate.dubbo.common.entity.City;
import com.bob.integrate.dubbo.common.service.CityDubboService;

/**
 * 只有 group，interface，version 是服务的匹配条件，三者决定是不是同一个服务，其它配置项均为调优和治理参数
 * 城市业务 Dubbo 服务层实现层
 */
@Service(version = "1.0.0", group = "default", filter = "loginCheck", delay = -1)
public class CityServiceProvider implements CityDubboService {

    @Override
    public City findCityByName(String cityName) {
        getAttachment();
        return new City(1L, 2L, "温岭", "是我的故乡");
    }

    /**
     * 获取客户端隐式传入的参数，用于框架集成，不建议常规业务使用
     */
    private void getAttachment() {
        System.out.println(RpcContext.getContext().getAttachment("attach"));
    }
}
