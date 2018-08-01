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
     * @param modelPaths
     * @param interfacePaths
     * @param mapperPaths
     */
    public ProgressCallbackComposite(Set<String> modelPaths, Set<String> interfacePaths, Set<String> mapperPaths) {
        //callbacks.add(new SuperClassAppender(modelPaths, interfacePaths));
        callbacks.add(new MapperMethodEditor(interfacePaths, mapperPaths));
    }

    @Override
    public void done() {
        for (ProgressCallback callback : callbacks) {
            callback.done();
        }
    }
}
