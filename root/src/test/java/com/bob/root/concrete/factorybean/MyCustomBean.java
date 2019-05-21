package com.bob.root.concrete.factorybean;

import lombok.Data;

/**
 * @author wb-jjb318191
 * @create 2019-05-21 10:49
 */
@Data
public class MyCustomBean {

    private String name;

    public MyCustomBean(String name) {
        this.name = name;
    }
}
