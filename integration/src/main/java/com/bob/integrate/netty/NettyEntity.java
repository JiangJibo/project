package com.bob.integrate.netty;

import java.io.Serializable;

/**
 * Netty测试实体类
 *
 * @author wb-jjb318191
 * @create 2018-04-13 14:51
 */
public class NettyEntity implements Serializable {

    private static final long serialVersionUID = -1049772597611393845L;

    public NettyEntity() {
    }

    public NettyEntity(String name) {
        this.data = name;
    }

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
