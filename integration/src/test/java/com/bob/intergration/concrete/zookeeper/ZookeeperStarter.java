package com.bob.intergration.concrete.zookeeper;

import java.util.Date;

import com.bob.integrate.zookeeper.ZkClientFactory;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2018-06-05 15:54
 */
public class ZookeeperStarter {

    private ZkClient zkClient;

    private static final String PERSISTENT_PATH = "/lanboal";
    private static final String EPHEMERAL_PATH = "/bob";

    @Before
    public void init() {
        zkClient = ZkClientFactory.getZkClient();
    }

    /**
     * 创建持久化节点
     */
    @Test
    public void createPersistent() {
        zkClient.createPersistent(PERSISTENT_PATH);
        System.out.println("创建持久化节点成功");
    }

    @Test
    public void getPersistent() {
        boolean exists = zkClient.exists(PERSISTENT_PATH);
        System.out.println(exists ? "存在持久化节点" : "不存在持久化节点");
    }

    @Test
    public void createEphemeral() {
        zkClient.createEphemeral(EPHEMERAL_PATH);
        System.out.println("创建临时节点成功");
    }

    @Test
    public void getEphemeral() {
        boolean exists = zkClient.exists(EPHEMERAL_PATH);
        System.out.println(exists ? "存在临时节点" : "不存在临时节点");
    }

    /**
     * 如果节点已存在,会抛出{@link ZkNodeExistsException}
     */
    @Test
    public void sendData() {
        String path = zkClient.create("/date", new Date(), CreateMode.PERSISTENT);
        System.out.println("存储数据后的path:" + path);
    }

    @Test
    public void getData() {
        Date result = zkClient.readData("/date");
        System.out.println("读取数据:[" + result.toString() + "]");
    }

}
