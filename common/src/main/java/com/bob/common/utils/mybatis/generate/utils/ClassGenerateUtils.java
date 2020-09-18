package com.bob.common.utils.mybatis.generate.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import com.bob.common.utils.mybatis.generate.callback.ServiceClassGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.UrlResource;

/**
 * @author wb-jjb318191
 * @create 2020-07-29 11:45
 */
public abstract class ClassGenerateUtils {

    public static final String OS = System.getProperty("os.name").toLowerCase();

    public static String getShortNameAsProperty(String className) {
        char[] cs = className.toCharArray();
        cs[0] += 32;
        return String.valueOf(cs);
    }

    /**
     * 测试时使用的获取模板内容
     *
     * @param template
     * @return
     * @throws IOException
     */
    public static String getTemplateContent(String template) throws IOException {
        return FileUtils.readFileToString(
            Paths.get("utils", "src", "main", "resources", template).toFile(), "UTF-8");
    }

    /**
     * 获取模板内容
     *
     * @param template
     * @return
     * @throws IOException
     */
    public static String extractTemplateContent(String template) throws IOException {
        URL url = ServiceClassGenerator.class.getClassLoader().getResource(template);
        return IOUtils.toString(new InputStreamReader(new UrlResource(url).getInputStream(), "UTF-8"));
    }

    public static String extractJavaPath(String path) {
        return path.substring(path.indexOf("src/main/java/") + 14, path.indexOf(".java")).replace("/", ".");
    }

    public static String formatNow() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String filterElement(Set<String> set, String ele) {
        String element = set.stream().filter(s -> s.contains(ele)).findFirst().get();

        if (element.startsWith("/") && isWindows()) {
            element = element.substring(element.indexOf("/") + 1);
        }
        return element;
    }

    /**
     * 提取主键
     *
     * @param fullMapperPath
     * @return
     * @throws IOException
     */
    public static String extractPrimaryKeyType(String fullMapperPath) throws IOException {
        String classLine = FileUtils.readLines(new File(fullMapperPath), "UTF-8").stream().filter(
            line -> line.startsWith("public interface")).findFirst().get();

        if (classLine.contains("extends")) {
            return classLine.substring(classLine.indexOf("<") + 1, classLine.indexOf(","));
        }

        String deleteLine = FileUtils.readLines(new File(fullMapperPath), "UTF-8").stream().filter(
            line -> line.contains("int deleteById")).findFirst().get();

        return deleteLine.substring(deleteLine.indexOf("(") + 1, deleteLine.indexOf("id") - 1);
    }

    public static boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;
    }

    public static boolean isMacOSX() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
    }

    public static boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

}
