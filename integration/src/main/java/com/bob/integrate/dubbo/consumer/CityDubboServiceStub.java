package com.bob.integrate.dubbo.consumer;

import com.bob.integrate.dubbo.common.entity.City;
import com.bob.integrate.dubbo.common.service.CityDubboService;

/**
 * 本地存根
 *
 * @author Administrator
 * @create 2018-06-01 23:19
 */
public class CityDubboServiceStub implements CityDubboService {

    private CityDubboService service;

    public CityDubboServiceStub(CityDubboService service) {
        this.service = service;
    }

    @Override
    public City findCityByName(String cityName) {
        System.out.println("**************** 测试Stub ****************");
        return service.findCityByName(cityName);
    }
}
