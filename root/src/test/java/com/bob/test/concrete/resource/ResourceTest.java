package com.bob.test.concrete.resource;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author dell-7359
 * @create 2017-10-24 19:58
 */
public class ResourceTest {

    public static final String SQLMAP_TARGETPACKAGE = "com.bob.mvc.mapper";



    /**
     * 使用'/'替换路径中的'.'
     *
     * @param path
     * @return
     */
    private String replaceDotByDelimiter(String path) {
        Assert.hasText(path, "替换路径不能为空");
        return StringUtils.replace(path, ".", "/");
    }
}
