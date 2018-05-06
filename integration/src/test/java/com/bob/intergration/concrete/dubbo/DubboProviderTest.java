package com.bob.intergration.concrete.dubbo;

import com.bob.integrate.dubbo.DubboContextConfig.ProviderContextConfig;
import com.bob.intergration.config.TestContextConfig;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Administrator
 * @create 2018-05-06 16:59
 */
@ContextConfiguration(classes = ProviderContextConfig.class)
public class DubboProviderTest extends TestContextConfig {

    @Test
    public void bootstrap() throws InterruptedException {
        System.out.println("启动Dubbo的生产者服务");
        Thread.sleep(1000 * 60);
    }

}
