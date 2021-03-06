package com.bob.common.utils.mybatis.generate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.bob.common.utils.mybatis.generate.callback.ProgressCallbackComposite;
import com.bob.common.utils.mybatis.generate.callback.SuperClassAppender;
import com.bob.common.utils.mybatis.generate.constant.GeneratorContextConfig;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Mybatis逆向工程执行者
 * 基于Mybatis Generator 1.3.5 Release
 *
 * @author wb-jjb318191
 * @create 2017-09-28 17:08
 */
public class MybatisGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisGenerator.class);

    /**
     * 生成的Model文件地址集合
     */
    private Set<String> generatedModelPaths = new HashSet<>();

    /**
     * 生成的Interface文件地址集合
     */
    private Set<String> generatedInterfacePaths = new HashSet<>();

    /**
     * 生成的Mapper.xml文件地址集合
     */
    private Set<String> generatedMapperPaths = new HashSet<>();

    private AtomicBoolean executed = new AtomicBoolean(false);

    /**
     * Main函数,执行逆向工程
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        MybatisGenerator.generate();
    }

    /**
     * 执行逆向工程
     * 使用配置好的执行策略{@linkplain GeneratorContextConfig}
     *
     * @throws Exception
     * @see GeneratorContextConfig
     */
    private static void generate() throws Exception {
        new MybatisGenerator().generate(GeneratorContextConfig.OVERRIDE_EXIST);
        //执行第二次的原因是为了让Mapper.xml里有两行注释,包围由逆向工程生成的元素
        new MybatisGenerator().generate(true);
    }

    /**
     * 执行逆向工程
     *
     * @param override 是否覆盖已存在的Model,Dao,Mapper
     * @throws Exception
     */
    private void generate(boolean override) throws Exception {
        if (!override & inspectGeneratedFilesExists()) {
            String over = GeneratorContextConfig.class.getSimpleName() + "." + "OVERRIDE_EXIST";
            throw new IllegalStateException(String.format("逆向工程生成的文件将会覆盖已存在文件，请确认做好备份后设置[%s]属性为true,执行后请还原为false", over));
        }
        Configuration config = new GeneratorConfigurationManager().configMybatisGenerator();
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, new DefaultShellCallback(true), new ArrayList<String>());
        myBatisGenerator.generate(new ProgressCallbackComposite(generatedModelPaths, generatedInterfacePaths, generatedMapperPaths));
    }

    /**
     * 检测将通过逆向工程生成的Model,Dao,Mapper是否已存在
     *
     * @throws Exception
     */
    private boolean inspectGeneratedFilesExists() throws Exception {
        //每次运行执行两次mybatis逆向工程,第二次时文件肯定已存在,不检查
        if (!executed.compareAndSet(false, true)) {
            return true;
        }
        LOGGER.info("非覆盖式执行Mybatis Generate,检查将要生成的文件是否已存在!");
        List<String> classNames = convertTableToClassName(GeneratorContextConfig.TABLES);

        String mapperPackage = replaceDotByDelimiter(GeneratorContextConfig.SQLMAP_TARGET_PACKAGE);

        String warnMsg = "即将覆盖{} [{}] ";
        boolean exists = false;
        for (String clazzName : classNames) {
            String modelName = GeneratorContextConfig.JAVA_MODEL_TARGET_PACKAGE + "." + clazzName;
            if (exists = isClassExists(modelName) || exists) {
                LOGGER.warn(warnMsg, "Model Class", modelName);
            }
            String daoName = GeneratorContextConfig.JAVACLIENT_TARGET_PACKAGE + "." + clazzName + "Mapper";
            if (exists = isClassExists(daoName) || exists) {
                LOGGER.warn(warnMsg, "DAO Class", daoName);
            }
            String mapperPath = mapperPackage + "/" + clazzName + "Mapper.xml";
            if (exists = isMapperExists(mapperPath) || exists) {
                LOGGER.warn(warnMsg, "Mapper XML", mapperPath);
            }
        }
        return exists;
    }

    /**
     * 依据驼峰原则格式化将表名转换为类名,当遇到下划线时去除下划线并对之后的一位字符大写
     *
     * @param tables
     * @return
     */
    private List<String> convertTableToClassName(List<String> tables) {
        List<String> classes = new ArrayList<String>();
        for (String table : tables) {
            classes.add(convertTableToClassName(table));
        }
        return classes;
    }

    /**
     * 依据驼峰原则格式化将表名转换为类名,当遇到下划线时去除下划线并对之后的一位字符大写
     *
     * @param table
     * @return
     */
    private String convertTableToClassName(String table) {
        Assert.hasText(table, "表名不能为空");
        StringBuilder sb = new StringBuilder();
        sb.append(toUpperCase(table.charAt(0)));
        for (int i = 1; i < table.length(); i++) {
            sb.append('_' == table.charAt(i) ? toUpperCase(table.charAt(++i)) : table.charAt(i));
        }
        return sb.toString();
    }

    /**
     * 将字符转换为大写
     *
     * @param ch
     * @return
     */
    private char toUpperCase(char ch) {
        return Character.toUpperCase(ch);
    }

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

    /**
     * 项目是否是多模块项目
     *
     * @return
     */
    private boolean isMultiModuleProject() {
        return !GeneratorContextConfig.DEFAULT_JAVA_TARGET_PROJECT.startsWith("src");
    }

    /**
     * 验证类是否存在
     *
     * @param className
     * @return
     */
    private boolean isClassExists(String className) throws IOException {
        Assert.hasText(className, "类名不能为空");
        String absPath = this.getRootPath() + "/" + GeneratorContextConfig.DEFAULT_JAVA_TARGET_PROJECT + "/" + replaceDotByDelimiter(className)
            + ".java";
        if (className.contains("Mapper")) {
            generatedInterfacePaths.add(absPath);
        } else {
            generatedModelPaths.add(absPath);
        }
        return new FileSystemResource(absPath).exists();
    }

    /**
     * 验证文件是否存在
     *
     * @param mapperPath
     * @return
     */
    public boolean isMapperExists(String mapperPath) throws IOException {
        Assert.hasText(mapperPath, "Mapper路径不能为空");
        String absPath = this.getRootPath() + "/" + GeneratorContextConfig.DEFAULT_RESOURCES_TARGET_PROJECT + "/" + mapperPath;
        generatedMapperPaths.add(absPath);
        return new FileSystemResource(absPath).exists();
    }

    /**
     * 获取项目根路径
     *
     * @return
     * @throws IOException
     */
    private String getRootPath() throws IOException {
        String classPath = this.replaceDotByDelimiter(this.getClass().getName()) + ".class";
        Resource resource = new ClassPathResource(classPath);
        String path = resource.getFile().getAbsolutePath();
        path = path.substring(0, path.indexOf("\\target"));
        return path.substring(0, path.lastIndexOf("\\"));
    }

}
