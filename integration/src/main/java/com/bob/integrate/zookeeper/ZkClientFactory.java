package com.bob.integrate.zookeeper;

import org.I0Itec.zkclient.ZkClient;

/**
 * 单机模式下的Zookeeper客户端
 *
 * @author wb-jjb318191
 * @create 2018-06-06 15:44
 */
public class ZkClientFactory {

    // 超时时间
    private static final int SESSION_TIMEOUT = 5000;
    // zookeeper server列表
    private static final String HOSTS = "localhost:2181,localhost:2182,localhost:2183";
    private static final String HOST = "localhost:2181";

    /**
     * @return
     */
    public static ZkClient getZkClient() {
        return new ZkClient(HOST, SESSION_TIMEOUT);
    }

}
