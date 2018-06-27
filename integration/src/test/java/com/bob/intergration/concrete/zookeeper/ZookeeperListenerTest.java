package com.bob.intergration.concrete.zookeeper;

import java.util.Timer;
import java.util.TimerTask;

import com.bob.integrate.zookeeper.DefaultChildListener;
import com.bob.integrate.zookeeper.DefaultDataListener;
import com.bob.integrate.zookeeper.DefaultStateListener;
import com.bob.integrate.zookeeper.ZkClientFactory;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Before;
import org.junit.Test;

/**
 * zookeeper监听器测试
 * 使用zkCli.cmd来操作zookeeper节点,触发相应的事件
 *
 * @author wb-jjb318191
 * @create 2018-06-08 10:12
 */
public class ZookeeperListenerTest {

    private ZkClient zkClient;
    private Timer timer;

    @Before
    public void init() {
        timer = new Timer();
        zkClient = ZkClientFactory.getZkClient();
    }

    /**
     * 监听子节点的创建,删除
     *
     * @throws InterruptedException
     */
    @Test
    public void testListeningChild() throws InterruptedException {
        zkClient.subscribeChildChanges("/bob", new DefaultChildListener());
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 监听节点数据
     * 通过zkCli.cmd操作设置的数据,传递到Java上会发生反序列化异常,因为没有Class信息
     *
     * @throws InterruptedException
     */
    @Test
    public void testListeningData() throws InterruptedException {
        zkClient.subscribeDataChanges("/bob", new DefaultDataListener());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                zkClient.writeData("/bob", "lanboal");
            }
        }, 5000);
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testListeningState() throws InterruptedException {
        zkClient.subscribeStateChanges(new DefaultStateListener());
        Thread.sleep(Integer.MAX_VALUE);
    }

}
