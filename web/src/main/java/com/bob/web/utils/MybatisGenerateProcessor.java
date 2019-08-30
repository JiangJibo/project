package com.bob.web.utils;

import com.bob.common.entity.base.BaseMapper;
import com.bob.common.entity.base.Paging;
import com.bob.common.utils.mybatis.generate.MybatisGenerator;
import com.bob.common.utils.mybatis.generate.constant.GenerateContextConfig.ContextConfigRefresher;

/**
 * @author wb-jjb318191
 * @create 2019-08-30 15:20
 */
public class MybatisGenerateProcessor {

    public static void main(String[] args) throws Exception {
        // 配置逆向工程上下文信息
        ContextConfigRefresher.newRefresher()
            .superModelName(Paging.class.getName())
            .superMapperName(BaseMapper.class.getName())
            .tables("seed")
            .driverPath("common/src/main/resources/mysql-connector-java-5.1.44-bin.jar")
            .jdbcConnectionUrl("jdbc:mysql://localhost:3306/project")
            .jdbcUserName("lanboal")
            .jdbcPassword("123456")
            .javaModelTargetProject("web/src/main/java")
            .javaModelTargetPackage("com.alibaba.sec.datapicker.model.dataobject")
            .javaMapperInterfaceTargetProject("web/src/main/java")
            .javaMapperInterfaceTargetPackage("com.alibaba.sec.datapicker.mapper")
            .sqlMapperTargetProject("web/src/main/resources")
            .sqlMapperTargetPackage("com.alibaba.sec.datapicker.mapper")
            .refresh();

        MybatisGenerator.generate();
    }

}
