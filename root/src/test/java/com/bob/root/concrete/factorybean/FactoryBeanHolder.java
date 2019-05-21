package com.bob.root.concrete.factorybean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wb-jjb318191
 * @create 2019-05-21 10:51
 */
@Data
public class FactoryBeanHolder {

    @Autowired
    private MyCustomBean myCustomBean;

}
