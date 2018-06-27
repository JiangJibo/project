package com.bob.integrate.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeperMain;

/**
 * 自定义观察者
 *
 * @author wb-jjb318191
 * @create 2018-06-05 15:55
 */
public class MyWatcher implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        ZooKeeperMain.printMessage("WATCHER::");
        ZooKeeperMain.printMessage(event.toString());
    }
}
