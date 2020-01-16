package com.bob.common.utils.mybatis.generate;

import com.bob.common.utils.mybatis.generate.constant.GenerateContextConfig;
import com.bob.common.utils.mybatis.generate.constant.GenerateContextConfig.ContextConfigRefresher;
import com.bob.common.utils.mybatis.generate.constant.GenerateContextConfig.Database;
import com.bob.common.utils.mybatis.generate.utils.BaseMapper;

/**
 * Mybatis逆向工程使用方法介绍
 * 目的：将数据库表映射成Model,MapperInterface,Mapper.xml。对上述数据做统一抽象，统一风格
 *
 * 1. 工程配置信息在 {@link GenerateContextConfig} 里, 通过 {@link ContextConfigRefresher} 来更新配置(见下文)
 * 因为这仅仅是一个工具,不考虑并发情况, 所以将配置都定义为静态属性, 简洁代码
 *
 * 2. 使用tddl中间件时, 因为不好直接定位其数据库连接地址, 所以需要将表结构导出到某个数据库。建议本地装个mysql, 将表写入本地库, 然后连接
 * 本地库来执行逆向工程
 *
 * @author wb-jjb318191
 * @create 2019-09-02 11:14
 */
public class UsageIntroduction {

    public static void main(String[] args) throws Exception {
        // 配置逆向工程上下文信息
        ContextConfigRefresher.newRefresher()
            .superMapperName(BaseMapper.class.getName())
            .tables("base_model")
            .jdbcConnectionUrl("jdbc:mysql://localhost:3306/project")
            .database(Database.MYSQL)
            .jdbcUserName("lanboal")
            .jdbcPassword("123456")
            .javaModelTargetProject("web/src/main/java")
            .javaModelTargetPackage("com.bob.web.mvc.entity.model")
            .javaMapperInterfaceTargetProject("web/src/main/java")
            .javaMapperInterfaceTargetPackage("com.bob.web.mvc.mapper")
            .sqlMapperTargetProject("web/src/main/resources")
            .sqlMapperTargetPackage("mapper")
            .refresh();

        MybatisGenerator.generate();
    }

}
