package com.bob.intergration.concrete.dubbo;

import com.bob.integrate.dubbo.consumer.CityServiceConsumer;
import com.bob.integrate.dubbo.consumer.DubboConsumerContextConfig;
import com.bob.intergration.config.TestContextConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Administrator
 * @create 2018-05-06 16:59
 */
@ContextConfiguration(classes = DubboConsumerContextConfig.class)
public class DubboConsumerTest extends TestContextConfig {

    @Autowired
    private CityServiceConsumer consumerService;

    @Test
    public void testRequestByRPC() {
        consumerService.printCity();
    }

}
