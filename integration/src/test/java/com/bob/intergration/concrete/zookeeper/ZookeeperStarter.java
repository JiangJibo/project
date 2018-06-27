package com.bob.intergration.concrete.zookeeper;

import java.util.Date;
import java.util.List;

import com.bob.integrate.zookeeper.ZkClientFactory;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.util.locale.provider.FallbackLocaleProviderAdapter;

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

    @Test
    public void testCount() {
        int count = zkClient.countChildren("/");
        System.out.println("根节点下有: " + count + "个元素");
    }

    /**
     * 创建有序节点时,获取到的顺序不一定就是创建时的顺序
     * 想要判断时序的话时需要遍历所有的元素，找到后缀最小的
     */
    @Test
    public void testCreateEphSeq() {
        zkClient.createPersistentSequential("/bob/bb", "bb");
        zkClient.createPersistentSequential("/bob/aa", "aa");
        zkClient.createPersistentSequential("/bob/dd", "dd");
        zkClient.createPersistentSequential("/bob/cc", "cc");
        List<String> children = zkClient.getChildren("/bob");
        for (String child : children) {
            System.out.println(child);
        }
    }

    @After
    public void destory() {
        zkClient.unsubscribeAll();
        zkClient.close();
    }

}
