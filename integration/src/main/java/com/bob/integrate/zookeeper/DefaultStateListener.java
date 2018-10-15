package com.bob.integrate.zookeeper;

import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher.Event.KeeperState;

/**
 * 状态监听器
 *
 * @author wb-jjb318191
 * @create 2018-06-08 10:02
 */
public class DefaultStateListener implements IZkStateListener {

    @Override
    public void handleStateChanged(KeeperState state) {
        System.out.println("当前状态:[" + state.name() + "]");
    }

    @Override
    public void handleNewSession() {
        System.out.println("重新建立连接!");
    }

    @Override
    public void handleSessionEstablishmentError(Throwable error) {
        System.out.println("建立连接失败:" + error.getMessage());
    }
}
