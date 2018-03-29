package com.bob.common.utils.mybatis.generate;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础Mapper，基础Model 设置类
 *
 * @author Administrator
 * @create 2018-03-29 22:47
 */
public class SuperClassAppender extends ProgressCallbackAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuperClassAppender.class);

    private Set<String> generatedFilePath;

    public SuperClassAppender(Set<String> generatedFilePath) {
        this.generatedFilePath = generatedFilePath;
    }

    @Override
    public void done() {

    }
}
