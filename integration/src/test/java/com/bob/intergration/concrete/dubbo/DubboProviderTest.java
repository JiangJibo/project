package com.bob.intergration.concrete.dubbo;

import com.bob.integrate.dubbo.provider.DubboProviderContextConfig;
import com.bob.intergration.config.TestContextConfig;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Administrator
 * @create 2018-05-06 16:59
 */
@ContextConfiguration(classes = DubboProviderContextConfig.class)
public class DubboProviderTest extends TestContextConfig {

    @Test
    public void bootstrap() throws InterruptedException {
        System.out.println("启动Dubbo的生产者服务");
        Thread.sleep(1000 * 60);
    }

}
