package com.bob.common.utils.mybatis.generate.constant;

import java.util.Arrays;
import java.util.List;

import com.bob.common.entity.base.BaseMapper;
import com.bob.common.entity.base.BaseModel;
import com.bob.common.utils.mybatis.generate.type.resolver.TinyintToIntegerResolver;
import org.mybatis.generator.api.JavaTypeResolver;

/**
 * Mybatis逆向工程配置
 * 表名太长可能导致MapperInterface和Mapper.xml内方法缺失
 * 若出现这种情况,建议先缩短表名,逆向工程完成后再手动还原,修改生成的相关类名
 *
 * @author wb-jjb318191
 * @create 2017-09-30 9:19
 */
public interface GeneratorContextConfig {

    //是否用逆向工程生成的Model,Dao,Mapper覆盖当前已存在的,若覆盖请做好备份工作
    Boolean OVERRIDE_EXIST = true;

    //指定要生成的Table
    List<String> TABLES = Arrays.asList("gsp_zip_zone");

    //连接数据库驱动包 这里选择自己本地位置,也可以将驱动放在项目的resources文件夹内
    String CLASSPATH_ENTRY = "common/src/main/resources/mysql-connector-java-5.1.44-bin.jar";
    //String CLASSPATH_ENTRY = "common/src/main/resources/postgresql-42.1.4.jar";

    //指定生成java文件的编码格式
    String JAVA_FILEEN_CODING = "UTF-8";

    //指定JDBC信息
    String JDBC_DRIVERCLASS = "com.mysql.jdbc.Driver";
    String JDBC_CONNECTIONURL = "jdbc:mysql://localhost:3306/project";
    String JDBC_USER_NAME = "root";
    String JDBC_PASSWORD = "lanboal";

    /*String JDBC_DRIVERCLASS = "org.postgresql.Driver";
    String JDBC_CONNECTIONURL = "jdbc:postgresql://rm-tatadminmap.pg.rdstest.tbsite.net:3432/campus_space";
    String JDBC_USER_NAME = "adminmap";
    String JDBC_PASSWORD = "adminmap";*/

    //如果maven工程只是单独的一个工程，targetProject="src/main/resources"
    //String DEFAULT_JAVA_TARGET_PROJECT = "src/main/java";
    //String DEFAULT_RESOURCES_TARGET_PROJECT = "src/main/resources";

    //若果maven工程是分模块的工程，即使时在当前模块下生产成Mybatis文件，也需要指定模块前缀，
    // targetProject="指定模块的名称/路径"，例如：targetProject="project-web/src/main/java"
    String DEFAULT_JAVA_TARGET_PROJECT = "web/src/main/java";
    //java类和配置文件生成位置可以指向不同的项目
    String DEFAULT_RESOURCES_TARGET_PROJECT = "web/src/main/resources";

    //指定Java Model生成位置
    String JAVA_MODEL_TARGET_PROJECT = DEFAULT_JAVA_TARGET_PROJECT;
    String JAVA_MODEL_TARGET_PACKAGE = "com.bob.web.mvc.entity.model";
    //指定Java DAO接口生成位置
    String JAVACLIENT_TARGET_PROJECT = DEFAULT_JAVA_TARGET_PROJECT;
    String JAVACLIENT_TARGET_PACKAGE = "com.bob.web.mvc.mapper";
    //指定Mapper.xml生成位置
    String SQLMAP_TARGET_PROJECT = DEFAULT_RESOURCES_TARGET_PROJECT;
    String SQLMAP_TARGET_PACKAGE = "com.bob.web.mvc.mapper";

    //是否为生成的Model添加父类
    boolean APPEND_SUPER_MODEL = false;
    String SUPER_MODEL_NAME = BaseModel.class.getName();
    //是否为生成的Mapper添加父类
    boolean APPEND_SUPER_MAPPER = false;
    String SUPER_MAPPER_NAME = BaseMapper.class.getName();

    /**
     * 可设置自定义的类型解析器
     */
    List<Class> TYPE_RESOLVER_CLASS = Arrays.asList(TinyintToIntegerResolver.class);

}