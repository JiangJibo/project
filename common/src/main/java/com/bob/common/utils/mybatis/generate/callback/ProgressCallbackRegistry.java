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
     * @param modelPaths      DO
     * @param mapperPaths     Mapper接口
     * @param mapperFilePaths Mapper.xml
     * @param servicePaths    Service
     * @param controllerPaths Controller
     */
    public ProgressCallbackRegistry(Set<String> modelPaths, Set<String> mapperPaths, Set<String> mapperFilePaths,
        Set<String> servicePaths, Set<String> controllerPaths) {

        callbacks.add(new SuperClassAppender(modelPaths, mapperPaths));
        callbacks.add(new MapperEditor(modelPaths, mapperPaths, mapperFilePaths));
        callbacks.add(new ServiceClassGenerator(modelPaths, mapperPaths, servicePaths));
        callbacks.add(new ControllerClassGenerator(modelPaths, mapperPaths, servicePaths, controllerPaths));

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
