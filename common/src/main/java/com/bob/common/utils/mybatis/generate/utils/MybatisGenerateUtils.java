package com.bob.common.utils.mybatis.generate.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.util.Assert;

/**
 * 逆向工程自定义工具类
 *
 * @author wb-jjb318191
 * @create 2019-05-14 14:54
 */
public abstract class MybatisGenerateUtils {

    /**
     * 获取指定路径的文件
     *
     * @param path
     * @return
     */
    public static File getFile(String path) {
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
    public static List<String> readFile(File file) {
        List<String> content;
        try {
            content = FileUtils.readLines(file, "UTF-8");
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("[%s]文件不可读", file.getAbsolutePath()), e);
        }
        return content;
    }

    /**
     * 将修改后的内容覆盖原先的
     *
     * @param file
     * @param lines
     */
    public static void writeFile(File file, List<String> lines) {
        try {
            FileUtils.writeLines(file, lines, false);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("写入[%s]文件出现异常"), e);
        }
    }

    /**
     * 添加Import行,import行的顺序不保证,自行格式化
     *
     * @param lines
     * @param className
     */
    public static void insertImportLine(List<String> lines, String className) {
        String importLine = "import " + className + ";";
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("import")) {
                lines.add(i, importLine);
                return;
            }
            //当碰到public时,说明到了Class定义行,终止循环
            if (line.startsWith("public")) {
                break;
            }
        }
        lines.add(2, importLine);
    }


}
