package com.bob.integrate.dubbo.common.entity;

/**
 * @author Administrator
 * @create 2018-06-02 19:01
 */
public class DubboException extends RuntimeException {

    public DubboException() {
        super();
    }

    public DubboException(String message) {
        super(message);
    }

    public DubboException(String message, Throwable cause) {
        super(message, cause);
    }

    public DubboException(Throwable cause) {
        super(cause);
    }
}
