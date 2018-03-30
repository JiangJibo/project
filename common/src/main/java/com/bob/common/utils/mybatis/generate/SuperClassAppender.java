package com.bob.common.utils.mybatis.generate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import static com.bob.common.utils.mybatis.generate.GeneratorContextConfig.APPEND_SUPER_MAPPER;
import static com.bob.common.utils.mybatis.generate.GeneratorContextConfig.APPEND_SUPER_MODEL;
import static com.bob.common.utils.mybatis.generate.GeneratorContextConfig.SUPER_MAPPER_NAME;
import static com.bob.common.utils.mybatis.generate.GeneratorContextConfig.SUPER_MODEL_NAME;

/**
 * 基础Mapper，基础Model 设置类
 *
 * @author Administrator
 * @create 2018-03-29 22:47
 */
class SuperClassAppender extends ProgressCallbackAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuperClassAppender.class);

    private Set<String> generatedFilePath;

    public SuperClassAppender(Set<String> generatedFilePath) {
        this.generatedFilePath = generatedFilePath;
        if (APPEND_SUPER_MODEL && !isClassExists(SUPER_MODEL_NAME)) {
            throw new IllegalStateException(String.format("[%s]不存在", SUPER_MODEL_NAME));
        }
        if (APPEND_SUPER_MAPPER && !isClassExists(SUPER_MAPPER_NAME)) {
            throw new IllegalStateException(String.format("[%s]不存在", SUPER_MAPPER_NAME));
        }
    }

    @Override
    public void done() {
        for (String path : generatedFilePath) {
            if (path.substring(0, path.lastIndexOf(".")).endsWith("Mapper") && APPEND_SUPER_MAPPER) {
                appendSuperMapper(path);
            } else if (APPEND_SUPER_MODEL) {
                appendSuperModel(path);
            }
        }
    }

    /**
     * 向指定的Java文件追加父类
     *
     * @param javaPath
     */
    private void appendSuperModel(String javaPath) {
        File model = getFile(javaPath);
        List<String> content = readFile(model);
        insertImportLine(content, SUPER_MODEL_NAME);
        insertSuperModel(content);
        writeFile(model, content);
    }

    /**
     * 向指定的Mapper接口追加父接口
     *
     * @param mapperPath
     */
    private void appendSuperMapper(String mapperPath) {
        File mapper = getFile(mapperPath);
        List<String> content = readFile(mapper);
        insertImportLine(content, SUPER_MAPPER_NAME);
        writeFile(mapper, insertSuperMapper(content));
    }

    /**
     * @param path
     * @return
     */
    private File getFile(String path) {
        Assert.hasText(path, "文件路径不能为空");
        File file = new File(path);
        Assert.isTrue(file.exists(), String.format("[%s]不存在", path));
        return file;
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @retur
     */
    private List<String> readFile(File file) {
        List<String> content;
        try {
            content = FileUtils.readLines(file, "UTF-8");
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("[%s]文件不可读", file.getAbsolutePath()), e);
        }
        return content;
    }

    /**
     * 添加Import行,import行的顺序不保证,自行格式化
     *
     * @param content
     * @param className
     */
    private void insertImportLine(List<String> content, String className) {
        String importLine = "import " + className + ";";
        for (int i = 0; i < content.size(); i++) {
            String line = content.get(i);
            if (line.startsWith("import")) {
                content.add(i, importLine);
                return;
            }
            //当碰到public时,说明到了Class定义行,终止循环
            if (line.startsWith("public")) {
                break;
            }
        }
        content.add(2, importLine);
    }

    /**
     * 将修改后的内容覆盖原先的
     *
     * @param file
     * @param content
     */
    private void writeFile(File file, List<String> content) {
        try {
            FileUtils.writeLines(file, content, false);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("写入[%s]文件出现异常"), e);
        }
    }

    /**
     * 插入 extends BaseModel
     *
     * @param content
     */
    private void insertSuperModel(List<String> content) {
        int classLineIndex = inspectClassLineIndex(content);
        String insertWords = "extends " + SUPER_MODEL_NAME.substring(SUPER_MODEL_NAME.lastIndexOf(".") + 1);
        String newClassLine = content.get(classLineIndex).replace("{", insertWords + " {");
        content.set(classLineIndex, newClassLine);
    }

    /**
     * 插入 extends BaseMapper<?,?>
     *
     * @param content
     */
    private List<String> insertSuperMapper(List<String> content) {
        int classLineIndex = inspectClassLineIndex(content);
        String key = getTypeString(content, "deleteByPrimaryKey");
        String target = getTypeString(content, "insertSelective");
        String insertWords = "extends " + SUPER_MAPPER_NAME.substring(SUPER_MAPPER_NAME.lastIndexOf(".") + 1) + "<" + key + "," + target + ">";
        String newClassLine = content.get(classLineIndex).replace("{", insertWords + " {");
        content = content.subList(0, classLineIndex);
        insertMapperComments(content, content.size() - 1);
        content.add(classLineIndex, newClassLine);
        content.add("}");
        return content;
    }

    /**
     * 为Mapper接口添加注释
     *
     * @param content
     * @param index
     */
    private void insertMapperComments(List<String> content, int index) {
        StringBuffer sb = new StringBuffer();
        String newLineWord = System.getProperty("line.separator");
        String dateLine = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        sb.append(newLineWord)
            .append("/**").append(newLineWord)
            .append(" * @author " + System.getenv("USERNAME")).append(newLineWord)
            .append(" * @create " + dateLine).append(newLineWord)
            .append(" */");
        content.add(index, sb.toString());
    }

    /**
     * 获取Mapper的Key,Target类型的字符串
     *
     * @param content
     * @param keywords
     * @return
     */
    private String getTypeString(List<String> content, String keywords) {
        for (String line : content) {
            if (line.contains(keywords)) {
                String argBody = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                return argBody.split(" ")[0];
            }
        }
        return null;
    }

    /**
     * 获取类定义行
     *
     * @param content
     * @return
     */
    private int inspectClassLineIndex(List<String> content) {
        int classLineIndex = 0;
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).startsWith("public")) {
                classLineIndex = i;
                break;
            }
        }
        return classLineIndex;
    }

    /**
     * @param className
     */
    private boolean isClassExists(String className) {
        return ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }

}
