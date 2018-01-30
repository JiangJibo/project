package com.bob.project.config.root.registrar.scan;

/**
 * Hsf提供Bean
 *
 * @author wb-jjb318191
 * @create 2018-01-09 17:12
 */
public class HSFSpringProviderBean {

    private Object target;
    private String serviceInterface;

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

}
