package com.bob.project.test.config;

import java.util.ArrayList;
import java.util.List;

import com.bob.project.utils.mybatis.generate.MybatisGenerateConfigs;
import com.bob.project.utils.mybatis.generate.MybatisGeneratorConfiguration;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Mybatis逆向工程解析器
 * 基于Mybatis Generator 1.3.5 Release
 *
 * @author wb-jjb318191
 * @create 2017-09-28 17:08
 */
public class MybatisGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisGenerator.class);

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
     * 使用配置好的执行策略{@linkplain MybatisGenerateConfigs}
     *
     * @throws Exception
     */
    public static void generate() throws Exception {
        new MybatisGenerator().generate(MybatisGenerateConfigs.OVERRIDE_EXIST);
    }

    /**
     * 执行逆向工程
     *
     * @param override 是否覆盖已存在的Model,Dao,Mapper
     * @throws Exception
     */
    private void generate(boolean override) throws Exception {
        if (!override) {
            inspectGeneratedFilesExists();
        }
        Configuration config = new MybatisGeneratorConfiguration().configMybatisGenerator();
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, new DefaultShellCallback(true), new ArrayList<String>());
        myBatisGenerator.generate(null);
    }

    /**
     * 检测将通过逆向工程生成的Model,Dao,Mapper是否已存在
     *
     * @throws Exception
     */
    private void inspectGeneratedFilesExists() throws Exception {
        LOGGER.info("非覆盖式执行Mybatis Generate,检查将要生成的文件是否已存在！");
        List<String> classNames = convertTableToClassName(MybatisGenerateConfigs.TABLES);

        String mapperPackage = replaceDotByDelimiter(MybatisGenerateConfigs.SQLMAP_TARGETPACKAGE);

        String remindMsg = "即将覆盖{}[{}],请确认做好备份后设置[{}]属性为true,执行后请还原为false";
        String override = MybatisGenerateConfigs.class.getSimpleName() + "." + "OVERRIDE_EXIST";
        boolean exists = false;
        for (String clazzName : classNames) {
            String modelName = MybatisGenerateConfigs.JAVA_MODEL_TARGETPACKAGE + "." + clazzName;
            if (exists = isClassExists(modelName)) {
                LOGGER.warn(remindMsg, "Model Class", modelName, override);
            }
            String daoName = MybatisGenerateConfigs.JAVACLIENT_TARGETPACKAGE + "." + clazzName + "Mapper";
            if (exists = isClassExists(daoName)) {
                LOGGER.warn(remindMsg, "DAO Class", daoName, override);
            }
            String mapperPath = mapperPackage + "/" + clazzName + "Mapper.xml";
            if (exists = isMapperExists(mapperPath)) {
                LOGGER.warn(remindMsg, "Mapper XML", mapperPath, override);
            }
        }
        if (exists) {
            throw new IllegalStateException("逆向工程生成的文件将会覆盖已存在文件，请备份好后设置覆盖式执行");
        }
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
     * 验证类是否存在
     *
     * @param className
     * @return
     */
    private boolean isClassExists(String className) {
        Assert.hasText(className, "类名不能为空");
        return ClassUtils.isPresent(className, this.getClass().getClassLoader());
    }

    /**
     * 验证文件是否存在
     *
     * @param mapperPath
     * @return
     */
    public boolean isMapperExists(String mapperPath) {
        Assert.hasText(mapperPath, "Mapper路径不能为空");
        return new ClassPathResource(mapperPath).exists();
    }

}
