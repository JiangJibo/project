package com.bob.common.utils.mybatis.generate.constant;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import com.alibaba.sec.yaxiangdi.mybatis.generate.type.JavaTypeResolverAdapter;
import com.alibaba.sec.yaxiangdi.mybatis.generate.type.resolver.TinyintToIntegerResolver;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * Mybatis逆向工程配置
 * 表名太长可能导致MapperInterface和Mapper.xml内方法缺失
 * 若出现这种情况,建议先缩短表名,逆向工程完成后再手动还原,修改生成的相关类名
 *
 * @author wb-jjb318191
 * @create 2017-09-30 9:19
 */
public abstract class GenerateContextConfig {

    public static boolean refreshed = false;

    /**
     * 是否用逆向工程生成的Model,Dao,Mapper覆盖当前已存在的,若覆盖请做好备份工作
     */
    public static boolean overrideExist = true;

    /**
     * 是否为生成的Model添加父类
     */
    public static boolean appendSuperModel = false;
    public static String superModelName;

    /**
     * 是否为生成的Mapper添加父类
     */
    public static boolean appendSuperMapper = true;
    public static String superMapperName;

    /**
     * 是否给java model 加上DO 后缀
     */
    public static boolean appendJavaModelDoSuffix = true;

    /**
     * 是否使用lombok的数据模型,即用@Data替代getter和setter方法
     */
    public static boolean useLombokDataModel = true;

    /**
     * 可设置自定义的类型解析器
     */
    public static List<Class<? extends JavaTypeResolverAdapter>> typeResolverClass;

    /**
     * 指定要生成的Table
     */
    @NotNull
    public static String[] tables;

    /**
     * 连接数据库驱动包 这里选择自己本地位置,也可以将驱动放在项目的resources文件夹内
     */
    @NotNull
    public static String driverClasspathEntry;

    /**
     * 指定生成java文件的编码格式
     */
    public static String javaFileCoding = "UTF-8";

    /**
     * 指定JDBC信息
     */
    @NotNull
    public static String jdbcDriverClass;
    @NotNull
    public static String jdbcConnectionUrl;
    @NotNull
    public static String jdbcUserName;
    @NotNull
    public static String jdbcPassword;

    //如果maven工程只是单独的一个工程，targetProject="src/main/resources"
    //String defaultJavaTargetProject = "src/main/java";
    //String defaultResourcesTargetProject = "src/main/resources";

    /**
     * 若果maven工程是分模块的工程，即使时在当前模块下生产成Mybatis文件，也需要指定模块前缀，
     * targetProject="指定模块的名称/路径"，例如：targetProject="project-web/src/main/java"
     * java类和配置文件生成位置可以指向不同的项目
     */

    /**
     * 指定Java Model生成位置
     */
    @NotNull
    public static String javaModelTargetProject;
    @NotNull
    public static String javaModelTargetPackage;

    /**
     * 指定Java DAO接口生成位置
     */
    @NotNull
    public static String javaMapperInterfaceTargetProject;
    @NotNull
    public static String javaMapperInterfaceTargetPackage;

    /**
     * 指定Mapper.xml生成位置
     */
    @NotNull
    public static String sqlMapperTargetProject;
    @NotNull
    public static String sqlMapperTargetPackage;

    /**
     * 上下文配置刷新器
     */
    public static class ContextConfigRefresher {

        public static ContextConfigRefresher newRefresher() {
            return new ContextConfigRefresher();
        }

        /**
         * 是否覆盖之前生成的
         *
         * @param overrideExist
         * @return
         */
        public ContextConfigRefresher overrideExist(boolean overrideExist) {
            GenerateContextConfig.overrideExist = overrideExist;
            return this;
        }

        /**
         * 是否添加父Model
         *
         * @param appendSuperModel
         * @return
         */
        public ContextConfigRefresher appendSuperModel(boolean appendSuperModel) {
            GenerateContextConfig.appendSuperModel = appendSuperModel;
            return this;
        }

        /**
         * 父Model类名称
         *
         * @param modelName
         * @return
         */
        public ContextConfigRefresher superModelName(String modelName) {
            GenerateContextConfig.superModelName = modelName;
            return this;
        }

        /**
         * 是否添加父Mapper
         *
         * @param appendSuperMapper
         * @return
         */
        public ContextConfigRefresher appendSuperMapper(boolean appendSuperMapper) {
            GenerateContextConfig.appendSuperMapper = appendSuperMapper;
            return this;
        }

        /**
         * 父Mapper名称
         *
         * @param mapperName
         * @return
         */
        public ContextConfigRefresher superMapperName(String mapperName) {
            GenerateContextConfig.superMapperName = mapperName;
            return this;
        }

        /**
         * 是否对Model添加DO后缀
         *
         * @param appendSuffix
         * @return
         */
        public ContextConfigRefresher appendJavaModelDoSuffix(boolean appendSuffix) {
            GenerateContextConfig.appendJavaModelDoSuffix = appendSuffix;
            return this;
        }

        /**
         * 是否使用Lombok风格
         *
         * @param useLombokDataModel
         * @return
         */
        public ContextConfigRefresher useLombokDataModel(boolean useLombokDataModel) {
            GenerateContextConfig.useLombokDataModel = useLombokDataModel;
            return this;
        }

        /**
         * 类型解析器
         *
         * @param typeResolverClasses
         * @return
         */
        public ContextConfigRefresher typeResolverClass(
            List<Class<? extends JavaTypeResolverAdapter>> typeResolverClasses) {
            GenerateContextConfig.typeResolverClass = typeResolverClasses;
            return this;
        }

        /**
         * 表名称
         *
         * @param tables
         * @return
         */
        public ContextConfigRefresher tables(String... tables) {
            GenerateContextConfig.tables = tables;
            return this;
        }

        /**
         * 指定数据库类型
         *
         * @param type
         * @return
         */
        public ContextConfigRefresher database(Database type) {
            GenerateContextConfig.driverClasspathEntry = type.driverClasspathEntry();
            // todo, mac下如何获取maven的m2地址
            if (!new File(driverClasspathEntry).exists()) {
                throw new IllegalArgumentException("未能取得驱动包位置,请联系开发人员!");
            }
            GenerateContextConfig.jdbcDriverClass = type.driverClass();
            return this;
        }

        /**
         * 数据库链接地址
         *
         * @param jdbcConnectionUrl
         * @return
         */
        public ContextConfigRefresher jdbcConnectionUrl(String jdbcConnectionUrl) {
            GenerateContextConfig.jdbcConnectionUrl = jdbcConnectionUrl;
            return this;
        }

        /**
         * 数据库账号
         *
         * @param jdbcUserName
         * @return
         */
        public ContextConfigRefresher jdbcUserName(String jdbcUserName) {
            GenerateContextConfig.jdbcUserName = jdbcUserName;
            return this;
        }

        /**
         * 数据库密码
         *
         * @param jdbcPassword
         * @return
         */
        public ContextConfigRefresher jdbcPassword(String jdbcPassword) {
            GenerateContextConfig.jdbcPassword = jdbcPassword;
            return this;
        }

        /**
         * Model生成的模块 格式为： "web/src/main/java" ,模块名称+/src/main/java
         *
         * @param javaModelTargetProject
         * @return
         */
        public ContextConfigRefresher javaModelTargetProject(String javaModelTargetProject) {
            GenerateContextConfig.javaModelTargetProject = javaModelTargetProject;
            return this;
        }

        /**
         * Model生成的包路径
         *
         * @param javaModelTargetPackage
         * @return
         */
        public ContextConfigRefresher javaModelTargetPackage(String javaModelTargetPackage) {
            GenerateContextConfig.javaModelTargetPackage = javaModelTargetPackage;
            return this;
        }

        /**
         * DAO接口生成的模块, 格式为： "web/src/main/java" ,模块名称+/src/main/java
         *
         * @param javaMapperInterfaceTargetProject
         * @return
         */
        public ContextConfigRefresher javaMapperInterfaceTargetProject(String javaMapperInterfaceTargetProject) {
            GenerateContextConfig.javaMapperInterfaceTargetProject = javaMapperInterfaceTargetProject;
            return this;
        }

        /**
         * DAO接口生成的包名称
         *
         * @param javaMapperInterfaceTargetPackage
         * @return
         */
        public ContextConfigRefresher javaMapperInterfaceTargetPackage(String javaMapperInterfaceTargetPackage) {
            GenerateContextConfig.javaMapperInterfaceTargetPackage = javaMapperInterfaceTargetPackage;
            return this;
        }

        /**
         * Mapper.xml 生成的模块, 格式为： "web/src/main/resources" ,模块名称+/src/main/resources
         *
         * @param sqlMapperTargetProject
         * @return
         */
        public ContextConfigRefresher sqlMapperTargetProject(String sqlMapperTargetProject) {
            GenerateContextConfig.sqlMapperTargetProject = sqlMapperTargetProject;
            return this;
        }

        /**
         * Mapper.xml 生成的包路径
         *
         * @param sqlMapperTargetPackage
         * @return
         */
        public ContextConfigRefresher sqlMapperTargetPackage(String sqlMapperTargetPackage) {
            GenerateContextConfig.sqlMapperTargetPackage = sqlMapperTargetPackage;
            return this;
        }

        /**
         * 初始化配置
         */
        public void refresh() {
            GenerateContextConfig.refreshed = true;
            ReflectionUtils.doWithLocalFields(GenerateContextConfig.class, field -> {
                if (field.isAnnotationPresent(NotNull.class)) {
                    field.setAccessible(true);
                    Object value = field.get(GenerateContextConfig.class);
                    Assert.notNull(value, String.format("配置[%s]不能为空", field.getName()));
                }
            });
            // 添加自定义类型解析器
            if (typeResolverClass == null) {
                typeResolverClass = Arrays.asList(TinyintToIntegerResolver.class);
            } else {
                typeResolverClass.add(TinyintToIntegerResolver.class);
            }
        }
    }

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface NotNull {

    }

    public static enum Database {

        MYSQL() {
            @Override
            String driverClass() {
                return "com.mysql.jdbc.Driver";
            }

            @Override
            String driverClasspathEntry() {
                // TODO 未想到如何读取pom里的配置信息,先固定版本
                String connectorVersion = "5.1.44";
                return System.getProperty("user.home") + "/.m2/repository/mysql/mysql-connector-java/"
                    + connectorVersion + "/mysql-connector-java-" + connectorVersion + ".jar";
            }
        };

        /**
         * 指定数据库驱动类
         *
         * @return
         */
        abstract String driverClass();

        /**
         * 驱动包路径
         *
         * @return
         */
        abstract String driverClasspathEntry();

    }

}
