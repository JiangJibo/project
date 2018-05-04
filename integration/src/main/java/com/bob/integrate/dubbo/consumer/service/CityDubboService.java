package com.bob.integrate.dubbo.consumer.service;

import com.bob.integrate.dubbo.common.entity.City;

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
