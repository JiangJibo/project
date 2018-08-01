package com.bob.common.utils.mybatis.generate.callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.ProgressCallback;

/**
 * 回调函数组合
 *
 * @author wb-jjb318191
 * @create 2018-08-01 9:59
 */
public class ProgressCallbackComposite extends ProgressCallbackAdapter {

    private List<ProgressCallback> callbacks = new ArrayList<>();

    /**
     * @param javaPaths
     * @param mapperPaths
     */
    public ProgressCallbackComposite(Set<String> javaPaths, Set<String> mapperPaths) {
        callbacks.add(new SuperClassAppender(javaPaths));
    }

    @Override
    public void done() {
        for (ProgressCallback callback : callbacks) {
            callback.done();
        }
    }
}
