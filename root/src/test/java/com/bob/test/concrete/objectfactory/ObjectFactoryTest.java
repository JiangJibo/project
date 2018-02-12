package com.bob.test.concrete.objectfactory;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.bob.test.config.TestContextConfig;
import org.junit.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ObjectFactory接口的测试
 *
 * @author wb-jjb318191
 * @create 2018-02-12 15:42
 */
public class ObjectFactoryTest extends TestContextConfig {

    @Autowired
    private HttpServletRequest request;

    /**
     * {@linkplain org.springframework.beans.factory.support.AutowireUtils#resolveAutowiringValue(Object, Class)}为
     * {@linkplain ObjectFactory} 生成动态代理类
     * 解决{@link javax.servlet.ServletRequest}类型的Bean的依赖注入问题
     */
    @Test
    public void init() {
        System.out.println(request.getClass().getInterfaces()[0].getName());
    }

}
