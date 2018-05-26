package com.bob.integrate.dubbo.consumer;

import com.alibaba.dubbo.config.annotation.Reference;

import com.bob.integrate.dubbo.common.entity.City;
import com.bob.integrate.dubbo.common.service.CityDubboService;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * 城市 Dubbo 服务消费者
 */
@Component
public class CityServiceConsumer {

    @Reference(version = "1.0.0", check = false,filter = "logging")
    private CityDubboService cityDubboService;

    public void printCity() {
        String cityName = "温岭";
        City city = cityDubboService.findCityByName(cityName);
        System.out.println(new Gson().toJson(city));
    }
}
