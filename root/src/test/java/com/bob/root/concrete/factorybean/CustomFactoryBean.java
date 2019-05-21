package com.bob.root.concrete.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author wb-jjb318191
 * @create 2019-05-21 10:48
 */
public class CustomFactoryBean implements FactoryBean<MyCustomBean> {

    @Override
    public MyCustomBean getObject() throws Exception {
        return new MyCustomBean("lanboal");
    }

    @Override
    public Class<?> getObjectType() {
        return MyCustomBean.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
