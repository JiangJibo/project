package com.bob.common.utils.mybatis.generate.callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.ProgressCallback;

import static com.bob.common.utils.mybatis.generate.constant.GenerateContextConfig.useLombokDataModel;

/**
 * 回调函数组合
 *
 * @author wb-jjb318191
 * @create 2018-08-01 9:59
 */
public class ProgressCallbackRegistry extends ProgressCallbackAdapter {

    private List<ProgressCallback> callbacks = new ArrayList<>();

    /**
     * @param modelPaths
     * @param interfacePaths
     * @param mapperPaths
     */
    public ProgressCallbackRegistry(Set<String> modelPaths, Set<String> interfacePaths, Set<String> mapperPaths) {
        callbacks.add(new SuperClassAppender(modelPaths, interfacePaths));
        callbacks.add(new MapperMethodEditor(interfacePaths, mapperPaths));
        if (useLombokDataModel) {
            callbacks.add(new LombokStyleManager(modelPaths));
        }
    }

    @Override
    public void done() {
        for (ProgressCallback callback : callbacks) {
            callback.done();
        }
    }
}
