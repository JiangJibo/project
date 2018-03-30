package com.bob.common.utils.mybatis.generate;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;

/**
 * Mybatis逆向工程基于Java形式的配置类
 *
 * @author wb-jjb318191
 * @create 2017-09-30 9:17
 */
class GeneratorConfigurationManager {

    public Configuration configMybatisGenerator() {
        Configuration configuration = new Configuration();
        configuration.addClasspathEntry(System.getProperty("user.dir") + "\\" + GeneratorContextConfig.CLASSPATH_ENTRY);

        Context context = new Context(null);
        context.setTargetRuntime("MyBatis3");
        context.setId("wb-jjb318191");

        context.addProperty("javaFileEncoding", GeneratorContextConfig.JAVA_FILEEN_CODING);

        //设置注解生成器
        context.setCommentGeneratorConfiguration(generateCommentConfiguration());
        //设置JDBC连接配置
        context.setJdbcConnectionConfiguration(generateJDBCConnectionConfiguration());
        //设置JDBC Type 与Java Type之间的映射解析器
        context.setJavaTypeResolverConfiguration(generateJavaTypeResolverConfiguration());
        //设置Java Model生成配置
        context.setJavaModelGeneratorConfiguration(generateJavaModelGeneratorConfiguration());
        //设置DAO层的生成配置
        context.setSqlMapGeneratorConfiguration(generateSqlMapGeneratorConfiguration());
        //设置Mapper.xml生成
        context.setJavaClientGeneratorConfiguration(generateJavaClientGeneratorConfiguration());
        //设置需要生成的Table及生成形式
        for (TableConfiguration tableConfiguration : generateTableConfigurations(context)) {
            context.addTableConfiguration(tableConfiguration);
        }
        configuration.addContext(context);
        return configuration;
    }

    /**
     * 配置注解生成器
     *
     * @return
     */
    private CommentGeneratorConfiguration generateCommentConfiguration() {
        CommentGeneratorConfiguration configuration = new CommentGeneratorConfiguration();
        configuration.setConfigurationType(GeneralCommentGenerator.class.getName());
        //是否去除自动生成的注释 true：是 ： false:否
        configuration.addProperty("suppressAllComments", "false");
        configuration.addProperty("addRemarkComments", "true");
        return configuration;
    }

    /**
     * 设置数据库连接的信息：驱动类、连接地址、用户名、密码
     *
     * @return
     */
    private JDBCConnectionConfiguration generateJDBCConnectionConfiguration() {
        JDBCConnectionConfiguration configuration = new JDBCConnectionConfiguration();
        configuration.setDriverClass(GeneratorContextConfig.JDBC_DRIVERCLASS);
        String jdbcSuffix = "?useUnicode=true&characterEncoding=UTF8&useSSL=false";
        configuration.setConnectionURL(GeneratorContextConfig.JDBC_CONNECTIONURL + jdbcSuffix);
        configuration.setUserId(GeneratorContextConfig.JDBC_USER_NAME);
        configuration.setPassword(GeneratorContextConfig.JDBC_PASSWORD);
        return configuration;
    }

    /**
     * 设置JDBC Type 与Java Type之间的映射解析器
     *
     * @return
     */
    private JavaTypeResolverConfiguration generateJavaTypeResolverConfiguration() {
        JavaTypeResolverConfiguration configuration = new JavaTypeResolverConfiguration();
        //可自定义类型映射解析器
        configuration.setConfigurationType(GeneratorContextConfig.JAVA_TYPE_RESOLVER);
        //默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和 NUMERIC 类型解析为java.math.BigDecimal
        configuration.addProperty("forceBigDecimals", "true");
        return configuration;
    }

    /**
     * 配置Java Model生成
     *
     * @return
     */
    private JavaModelGeneratorConfiguration generateJavaModelGeneratorConfiguration() {
        JavaModelGeneratorConfiguration configuration = new JavaModelGeneratorConfiguration();
        configuration.setTargetProject(GeneratorContextConfig.JAVA_MODEL_TARGET_PROJECT);
        configuration.setTargetPackage(GeneratorContextConfig.JAVA_MODEL_TARGET_PACKAGE);
        //是否让schema作为包的后缀
        configuration.addProperty("enableSubPackages", "false");
        //从数据库返回的值被清理前后的空格
        configuration.addProperty("trimStrings", "true");
        return configuration;
    }

    /**
     * 配置Mapper.xml生成
     *
     * @return
     */
    private SqlMapGeneratorConfiguration generateSqlMapGeneratorConfiguration() {
        SqlMapGeneratorConfiguration configuration = new SqlMapGeneratorConfiguration();
        configuration.setTargetProject(GeneratorContextConfig.SQLMAP_TARGET_PROJECT);
        configuration.setTargetPackage(GeneratorContextConfig.SQLMAP_TARGET_PACKAGE);
        //是否让schema作为包的后缀
        configuration.addProperty("enableSubPackages", "false");
        return configuration;
    }

    /**
     * 设置DAO生成
     *
     * @return
     */
    private JavaClientGeneratorConfiguration generateJavaClientGeneratorConfiguration() {
        JavaClientGeneratorConfiguration configuration = new JavaClientGeneratorConfiguration();
        configuration.setConfigurationType("XMLMAPPER");
        configuration.setTargetProject(GeneratorContextConfig.JAVACLIENT_TARGET_PROJECT);
        configuration.setTargetPackage(GeneratorContextConfig.JAVACLIENT_TARGET_PACKAGE);
        //是否让schema作为包的后缀
        configuration.addProperty("enableSubPackages", "false");
        return configuration;
    }

    private List<TableConfiguration> generateTableConfigurations(Context context) {
        List<TableConfiguration> configurations = new ArrayList<TableConfiguration>();
        for (String table : GeneratorContextConfig.TABLES) {
            TableConfiguration configuration = new TableConfiguration(context);
            configuration.setTableName(table);
            configuration.setSelectByExampleStatementEnabled(false);
            configuration.setDeleteByExampleStatementEnabled(false);
            configuration.setCountByExampleStatementEnabled(false);
            configuration.setUpdateByExampleStatementEnabled(false);
            configurations.add(configuration);
        }
        return configurations;
    }

}
