package com.bob.integrate.zookeeper;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;

/**
 * 子目录监听器
 *
 * @author wb-jjb318191
 * @create 2018-06-08 10:00
 */
public class DefaultChildListener implements IZkChildListener {

    @Override
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
        System.out.println(String.format("父节点:[%s]", parentPath));
        StringBuilder builder = new StringBuilder();
        for (String child : currentChilds) {
            builder.append(child).append(";");
        }
        System.out.println(String.format("当前子节点集合:[%s]", builder.toString()));
    }
}
