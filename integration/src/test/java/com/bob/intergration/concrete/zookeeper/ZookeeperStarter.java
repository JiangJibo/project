package com.bob.intergration.concrete.zookeeper;

import java.io.IOException;

import com.bob.integrate.zookeeper.MyWatcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2018-06-05 15:54
 */
public class ZookeeperStarter {

    @Test
    public void startZookeeper() throws IOException {
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 3000, new MyWatcher(), true);
    }

}
