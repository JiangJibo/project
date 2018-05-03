package com.bob.intergrate.dubbo.client.service;

import com.alibaba.dubbo.config.annotation.Reference;

import com.bob.intergrate.dubbo.entity.City;
import org.springframework.stereotype.Component;

/**
 * 城市 Dubbo 服务消费者
 *
 */
@Component
public class CityDubboConsumerService {

    @Reference(version = "1.0.0")
    CityDubboService cityDubboService;

    public void printCity() {
        String cityName = "温岭";
        City city = cityDubboService.findCityByName(cityName);
        System.out.println(city.toString());
    }
}
