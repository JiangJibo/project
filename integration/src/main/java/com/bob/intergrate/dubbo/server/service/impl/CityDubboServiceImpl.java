package com.bob.intergrate.dubbo.server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.bob.intergrate.dubbo.entity.City;
import com.bob.intergrate.dubbo.server.service.CityDubboService;

/**
 * 城市业务 Dubbo 服务层实现层
 */
@Service(version = "1.0.0")
public class CityDubboServiceImpl implements CityDubboService {

    @Override
    public City findCityByName(String cityName) {
        return new City(1L, 2L, "温岭", "是我的故乡");
    }
}
