package com.bob.intergration.concrete.zookeeper;

import com.bob.integrate.zookeeper.ZkClientFactory;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Before;

/**
 * AccessControl,存储控制测试
 *
 * @author wb-jjb318191
 * @create 2018-06-08 11:33
 */
public class ZookeeperACLTest {

    private ZkClient zkClient;

    @Before
    public void init() {
        zkClient = ZkClientFactory.getZkClient();
    }
}
