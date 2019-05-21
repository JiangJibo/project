package com.bob.root.concrete.factorybean;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author wb-jjb318191
 * @create 2019-05-21 10:50
 */
public class FactoryBeanApplication {

    @Test
    public void testResolveFactoryBeanReference() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(CustomFactoryBean.class, FactoryBeanHolder.class);
        FactoryBeanHolder factoryBeanHolder = applicationContext.getBean("factoryBeanHolder", FactoryBeanHolder.class);
        MyCustomBean myCustomBean = factoryBeanHolder.getMyCustomBean();
        System.out.println(myCustomBean.getName());
    }

}
