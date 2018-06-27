package com.bob.intergration.concrete.dubbo;

import com.alibaba.dubbo.common.bytecode.Wrapper;

import com.bob.integrate.dubbo.common.service.CityDubboService;
import org.junit.Test;

/**
 * @author Administrator
 * @create 2018-05-16 20:23
 */
public class WrapperTest {

    @Test
    public void testWrapperInterface() {
        Wrapper wrapper = Wrapper.getWrapper(CityDubboService.class);
        System.out.println(wrapper.getClass().getName());
        System.out.println(wrapper.getMethodNames().toString());
    }

}
