package com.bob.web.mvc.controller;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wb-jjb318191
 * @create 2019-01-17 16:45
 */
@RestController
@RequestMapping
public class MemoryTestController {

    @PostConstruct
    public void init() {

        byte[] b = new byte[10 * 1024 * 1024];
        System.out.println("分配了10M空间给数组");


        System.out.println("Xmx=" + Runtime.getRuntime().maxMemory() / 1024.0 / 1024 + "M");     //系统的最大空间

        System.out.println("free mem=" + Runtime.getRuntime().freeMemory() / 1024.0 / 1024 + "M");   //系统的空闲空间

        System.out.println("total mem=" + Runtime.getRuntime().totalMemory() / 1024.0 / 1024 + "M");   //当前可用的总空间

        System.gc();

        System.out.println("Xmx=" + Runtime.getRuntime().maxMemory() / 1024.0 / 1024 + "M");     //系统的最大空间

        System.out.println("free mem=" + Runtime.getRuntime().freeMemory() / 1024.0 / 1024 + "M");   //系统的空闲空间

        System.out.println("total mem=" + Runtime.getRuntime().totalMemory() / 1024.0 / 1024 + "M");   //当前可用的总空间
    }

}
