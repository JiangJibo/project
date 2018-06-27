package com.bob.integrate.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;

/**
 * 数据监听器
 *
 * @author wb-jjb318191
 * @create 2018-06-08 10:01
 */
public class DefaultDataListener implements IZkDataListener {

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        System.out.println(String.format("节点:[%s]数据发生变化，数据为:[%s]", dataPath, data));
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        System.out.println(String.format("[%s]节点被删除", dataPath));
    }
}
