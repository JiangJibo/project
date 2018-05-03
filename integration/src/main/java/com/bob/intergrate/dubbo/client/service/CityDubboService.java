package com.bob.intergrate.dubbo.client.service;

import com.bob.intergrate.dubbo.entity.City;

/**
 * 城市业务 Dubbo 服务层
 *
 */
public interface CityDubboService {

    /**
     * 根据城市名称，查询城市信息
     * @param cityName
     */
    City findCityByName(String cityName);
}
