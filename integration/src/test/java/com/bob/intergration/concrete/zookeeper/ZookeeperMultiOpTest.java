package com.bob.intergration.concrete.zookeeper;

import java.util.Arrays;
import java.util.List;

import com.bob.integrate.zookeeper.ZkClientFactory;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.ZooDefs;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Administrator
 * @create 2018-06-10 17:13
 */
public class ZookeeperMultiOpTest {

    private ZkClient zkClient;

    @Before
    public void init() {
        zkClient = ZkClientFactory.getZkClient();
    }

    @Test
    public void testMultiOp() {
        Op createParent = Op.create("/multi", "事务操作".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        Op create0 = Op.create("/multi/0", "事务操作0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        Op create1 = Op.create("/multi/1", "事务操作1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zkClient.multi(Arrays.asList(createParent, create0, create1));
    }

    @Test
    public void testGetMultiChild() {
        List<String> children = zkClient.getChildren("/multi");
        for (String child : children) {
            System.out.println(child);
        }
    }

}
