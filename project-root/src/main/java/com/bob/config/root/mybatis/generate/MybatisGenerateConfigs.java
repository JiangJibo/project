package com.bob.config.root.mybatis.generate;

import java.util.Arrays;
import java.util.List;

import org.mybatis.generator.api.JavaTypeResolver;

/**
 * Mybatis逆向工程配置
 *
 * @author wb-jjb318191
 * @create 2017-09-30 9:19
 */
public interface MybatisGenerateConfigs {

    //是否用逆向工程生成的Model,Dao,Mapper覆盖当前已存在的,若覆盖请做好备份工作
    Boolean OVERRIDE_EXIST = false;

    //指定要生成的Table
    List<String> TABLES = Arrays.asList("bank_user", "bank_account");

    //连接数据库驱动包 这里选择自己本地位置
    String CLASSPATH_ENTRY = "D:/profile/mysql-connector-java-5.1.44-bin.jar";
    //指定生成java文件的编码格式
    String JAVA_FILEEN_CODING = "UTF-8";

    //指定JDBC信息
    String JDBC_DRIVERCLASS = "com.mysql.jdbc.Driver";
    String JDBC_CONNECTIONURL = "jdbc:mysql://localhost:3306/project";
    String JDBC_USER_NAME = "root";
    String JDBC_PASSWORD = "lanboal";

    //如果maven工程只是单独的一个工程，targetProject="src/main/resources"
    //若果maven工程是分模块的工程，targetProject="所属模块的名称"，例如：targetProject="ecps-manager-mapper"
    String DEFAULT_JAVA_TARGETPROJECT = "src/main/java";
    String DEFAULT_RESOURCES_TARGETPROJECT = "src/main/resources";

    //指定Java Model生成位置
    String JAVA_MODEL_TARGETPROJECT = DEFAULT_JAVA_TARGETPROJECT;
    String JAVA_MODEL_TARGETPACKAGE = "com.bob.mvc.model";
    //指定Java DAO接口生成位置
    String JAVACLIENT_TARGETPROJECT = DEFAULT_JAVA_TARGETPROJECT;
    String JAVACLIENT_TARGETPACKAGE = "com.bob.mvc.mapper";
    //指定Mapper.xml生成位置
    String SQLMAP_TARGETPROJECT = DEFAULT_RESOURCES_TARGETPROJECT;
    String SQLMAP_TARGETPACKAGE = "com.bob.mvc.mapper";

    /**
     * 可设置自定义的类型解析器
     * {@linkplain JavaTypeResolver}
     */
    String JAVA_TYPE_RESOLVER = null;

}