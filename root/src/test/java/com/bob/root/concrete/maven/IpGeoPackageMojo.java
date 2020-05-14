package com.bob.root.concrete.maven;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

//import org.apache.maven.plugin.AbstractMojo;
//import org.apache.maven.plugins.annotations.Mojo;
//import org.apache.maven.plugins.annotations.Parameter;

//@Mojo(name = "IpGeoPackage")
//public class IpGeoPackageMojo extends AbstractMojo {
public class IpGeoPackageMojo {

    /**
     * 输入目录
     */
    private static final String INPUT_DIR = "input";

    /**
     * 输出目录
     */
    private static final String OUTPUT_DIR = "output";

    //@Parameter(property = "j2c.config.dir")
    private String j2cConfigDir;

    //@Parameter(property = "project.groupId")
    private String groupId;

    //@Parameter(property = "project.artifactId")
    private String artifactId;

    //@Parameter(property = "project.version")
    private String version;

    /**
     * 定义m2 repository路径, 如果不存在默认用System.getProperty("user.home") + /.m2/repository
     */
    //@Parameter(property = "m2.repository")
    private String m2Repository;

    public void execute() {
        System.out.println("########################################## IP-GEO-PACKAGE-PLUGIN ##############################################");

        initJ2cConfigDir();

        File jarFile = new File(buildJarPath());
        if (jarFile == null || !jarFile.exists()) {
            System.err.println("文件不存在");
            return;
        }
        File zipFile = null;
        try {
            zipFile = new File(j2cConfigDir, "output.zip");
            if (zipFile.exists()) {
                zipFile.delete();
            }
            // 拷贝原始jar包
            Files.copy(Paths.get(jarFile.getAbsolutePath()),
                Paths.get(j2cConfigDir + File.separator + INPUT_DIR, jarFile.getName()),
                StandardCopyOption.REPLACE_EXISTING);
            // 执行名称
            executeJ2cCommand();
            // 解压移除无关文件
            List<File> files = process(zipFile, j2cConfigDir + File.separator + OUTPUT_DIR, "jar", "so", "log");
            // 校验j2c加密结果
            validateJava2CEncryption(files);
            // 拷贝SO和JAR 到 输出 目录
            copyOutputToResources(buildOutputCopyPath(),files);
        } catch (Exception e) {
            throw new IllegalStateException("failed to execute ip-geo-package-plugin", e);
        } finally {
            if (zipFile != null) {
                zipFile.delete();
            }
        }
        System.out.println("############################################ IP-GEO-PACKAGE-PLUGIN ############################################");
    }

    /**
     * 初始化j2c文件夹路径
     */
    private void initJ2cConfigDir() {
        if (j2cConfigDir == null) {
            j2cConfigDir = Paths.get("").toAbsolutePath().toString();
            j2cConfigDir = j2cConfigDir.substring(0, j2cConfigDir.lastIndexOf(File.separator));
            j2cConfigDir = j2cConfigDir + File.separator + "ip-geo-package-plugin" + File.separator +"j2c";
            System.out.println("j2cConfigDir:" + j2cConfigDir);
        }
    }

    private Path buildOutputCopyPath(){
        String currentPath = Paths.get("").toAbsolutePath().toString();
        String root = currentPath.substring(0, currentPath.lastIndexOf(File.separator));
        return Paths.get(root,"classes","artifacts","ip_geo_fastclient_j2c_jar",version);
    }

    /**
     * 构建jar包路径
     *
     * @return
     */
    private String buildJarPath() {
        String repositoryPath = m2Repository;
        if (repositoryPath == null) {
            repositoryPath = System.getProperty("user.home") + File.separator + ".m2" + File.separator + "repository";
        }
        String jarPath = repositoryPath + File.separator + groupId.replace(".", File.separator)
            + File.separator + artifactId + File.separator + version + File.separator + artifactId + "-" + version
            + ".jar";

        System.out.println("ip-geo-fastclient jar path: " + jarPath);
        return jarPath;
    }

    /**
     * 执行j2c命令
     *
     * @throws InterruptedException
     * @throws IOException
     */
    private void executeJ2cCommand() throws InterruptedException, IOException {
        String jar = j2cConfigDir + File.separator + "JProtectCl.jar";
        String config = j2cConfigDir + File.separator + "configs" + File.separator + "jarConfigIpClient.data";
        String input = j2cConfigDir + File.separator + INPUT_DIR;
        String output = j2cConfigDir;
        String cmd = String.format(
            "java -jar " + jar + " --config=" + config + " --inputdir=" + input + " --outputdir=" + output);
        System.out.println("exec command :" + cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
    }

    /**
     * 验证j2c加密结果
     *
     * @param files
     */
    private void validateJava2CEncryption(List<File> files) throws IOException, ClassNotFoundException {
        File jarFile = null;
        for (File file : files) {
            if (file.getName().endsWith("jar")) {
                jarFile = file;
                break;
            }
        }
        if (jarFile == null) {
            System.err.println("不存在待j2c校验的jar包");
            return;
        }
        String tempDirPath = this.j2cConfigDir + File.separator + "temp";
        Path tempDir = Paths.get(tempDirPath);
        if (Files.notExists(tempDir)) {
            Files.createDirectory(tempDir);
        } else {
            removeFilesInDir(tempDirPath, null);
        }
        Path tempJarPath = Files.createFile(Paths.get(tempDirPath, jarFile.getName()));
        // 把jar包拷贝到临时文件夹
        Files.copy(new FileInputStream(jarFile), tempJarPath, StandardCopyOption.REPLACE_EXISTING);
        // 解压jar包
        unzip(tempJarPath.toFile(), tempDirPath);
        Path configFilePath = Paths.get(j2cConfigDir, "configs", "jarConfigIpClient.data");

        URLClassLoader classLoader = new URLClassLoader(new URL[] {new URL("file:" + jarFile.getAbsolutePath())},
            Thread.currentThread().getContextClassLoader());

        // 校验加密类
        Set<String> translateClassNames = extractElementValues(configFilePath, "translateClasses");
        Set<String> keepClassNames = extractElementValues(configFilePath, "keepClasses");

        Map.Entry<Set<String>, Set<String>> methodResult = validateTranslateClasses(classLoader, tempDir,
            translateClassNames, keepClassNames);

        // 应该加密而未加密的方法
        Set<String> unTranslateMethods = methodResult.getKey();
        // 不该加密而加密的方法
        Set<String> translateMethods = methodResult.getValue();

        // 校验加密方法
        Set<String> translateMethodNames = extractElementValues(configFilePath, "translateMethods");
        Set<String> keepMethodNames = extractElementValues(configFilePath, "keepMethods");
        // 校验加密方法, 需要配合加密类的遗留方法
        try {
            validateTranslateMethods(classLoader, translateMethodNames, translateMethods, keepMethodNames,
                unTranslateMethods);
        } finally {
            // 清除temp文件夹
            removeFilesInDir(tempDirPath, null);
        }
    }

    /**
     * 解压Zip文件
     *
     * @param file
     * @return 解压后的目录
     * @throws IOException
     */
    public List<File> process(File file, String unzipDir, String... retainFileSuffixes) throws Exception {
        removeFilesInDir(unzipDir, null);
        unzip(file, unzipDir);
        List<File> retainFiles = listSpecifiedFiles(unzipDir, Arrays.asList(retainFileSuffixes));
        removeFilesInDir(unzipDir, retainFiles);
        return retainFiles;
    }

    /**
     * 解压文件
     *
     * @param file
     * @param unzipDir
     * @throws IOException
     */
    private void unzip(File file, String unzipDir) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()) {
            process(zipFile, enumeration.nextElement(), unzipDir);
        }
    }

    /**
     * 穷举指定目录下的指定类型的文件
     *
     * @param dirPath  指定目录
     * @param suffixes
     * @return
     */
    private List<File> listSpecifiedFiles(String dirPath, List<String> suffixes) {
        List<File> all = new ArrayList<>();
        introspectFiles(dirPath, all);
        List<File> result = new ArrayList<>(all.size());
        for (File file : all) {
            if (suffixes.contains(file.getName().substring(file.getName().lastIndexOf(".") + 1))) {
                result.add(file);
            }
        }
        return result;
    }

    /**
     * 移除目录以及其下的所有文件
     *
     * @param path
     */
    private boolean removeFilesInDir(String path, List<File> excludes) {
        boolean deleteAll = true;
        File dir = new File(path);
        if (!dir.exists()) {
            return true;
        }
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                boolean del = removeFilesInDir(file.getPath(), excludes);
                if (del) {
                    file.delete();
                }
                continue;
            }
            boolean exclude = false;
            if (excludes != null) {
                for (File f : excludes) {
                    if (f.getName().equals(file.getName())) {
                        exclude = true;
                        break;
                    }
                }
            }
            if (exclude) {
                deleteAll = false;
            } else {
                file.delete();
            }
        }
        return deleteAll;
    }

    /**
     * 内省指定目录下的所有文件
     *
     * @param dirPath 目录
     * @param list
     */
    private void introspectFiles(String dirPath, List<File> list) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                introspectFiles(file.getAbsolutePath(), list);
            }
            list.add(file);
        }
    }

    /**
     * 解压指定zip元素
     *
     * @param zipFile
     * @param entry
     * @param parentPath
     * @throws IOException
     */
    private void process(ZipFile zipFile, ZipEntry entry, String parentPath) {
        String fileName = entry.getName();
        File file = new File(parentPath + File.separator + fileName);
        // 如果当前元素是目录
        if (entry.isDirectory()) {
            if (!file.exists()) {
                file.mkdirs();
            }
            return;
        }
        // 当前元素是文件,但可能上级目录不存在
        if (fileName.contains("/")) {
            String subPath = parentPath + File.separator + entry.getName().substring(0,
                entry.getName().lastIndexOf("/"));
            // 如果上级目录不存在
            File parent = new File(subPath);
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
        try {
            Files.copy(zipFile.getInputStream(entry), Paths.get(file.getAbsolutePath()));
        } catch (Exception e) {
            System.err.println(String.format("unzip zip file: %s inner file: %s failed, exception message: %s", zipFile.getName(), entry.getName(), e.getMessage()));
        }
    }

    /**
     * 读取配置文件里的内容, 配置文件需严格验证JSON格式：
     * "translateMethods":[
     * "com.alibaba.sec.client.impl.IPv4GeoClient.load",
     * "com.alibaba.sec.client.impl.IPv6GeoClient.load",
     * "com.alibaba.sec.client.FastIPGeoClient.getIPGeoClient"
     * ]
     *
     * @param file
     * @param element
     * @return
     * @throws IOException
     */
    private Set<String> extractElementValues(Path file, String element) throws IOException {
        Set<String> result = new HashSet<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            boolean start = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(element) && line.endsWith("[")) {
                    start = true;
                    continue;
                } else if (start && line.contains("]")) {
                    break;
                }
                if (start) {
                    result.add(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")));
                }
            }
        }
        return result;
    }

    /**
     * 校验翻译类
     *
     * @param classLoader
     * @param dir            jar解压目录
     * @param classNames     待校验类名称
     * @param keepClassNames 保留类名称
     */
    private Map.Entry<Set<String>, Set<String>> validateTranslateClasses(URLClassLoader classLoader, Path dir,
        Set<String> classNames, Set<String> keepClassNames) throws ClassNotFoundException {

        Set<String> translateClasses = loadClasses(dir, classNames);
        Set<String> keepClasses = loadClasses(dir, keepClassNames);
        translateClasses.removeAll(keepClasses);
        // 校验加密方法
        Set<Method> unTranslateMethods = validateTranslateMethods(classLoader, translateClasses);
        // 校验保留方法
        Set<Method> translateMethods = validateKeepMethods(classLoader, keepClasses);
        return new SimpleEntry<>(extractMethodNames(unTranslateMethods), extractMethodNames(translateMethods));
    }

    private Set<String> extractMethodNames(Set<Method> methods) {
        Set<String> result = new HashSet<>();
        for (Method method : methods) {
            result.add(method.getDeclaringClass().getName() + "." + method.getName());
        }
        return result;
    }

    /**
     * 加载指定类名
     *
     * @param dir
     * @param classOrPackageNames
     * @return
     */
    private Set<String> loadClasses(Path dir, Set<String> classOrPackageNames) {
        Set<String> processedClassNames = new HashSet<>();
        for (String cname : classOrPackageNames) {
            String simpleName = cname.substring(cname.lastIndexOf(".") + 1);
            // 如果最后一段的首字母是小写的 || 或者是'*' 也就是包
            if (Character.isLowerCase(simpleName.charAt(0))) {
                processedClassNames.addAll(extractClassNamesFromTargetDirectory(dir, simpleName));
            }
            // 以 * 结尾的包
            else if (simpleName.charAt(0) == '*') {
                processedClassNames.addAll(
                    extractClassNamesFromTargetDirectory(dir, cname.substring(0, cname.lastIndexOf("."))));
            } else {
                processedClassNames.add(cname);
            }
        }
        return processedClassNames;
    }

    /**
     * 从目录提取指定包下的Class名称
     *
     * @param dir
     * @param pkg
     * @return
     */
    private List<String> extractClassNamesFromTargetDirectory(Path dir, String pkg) {
        String innerPath = pkg.replace(".", File.separator);
        Path innerDir = Paths.get(dir.toAbsolutePath().toString(), innerPath);
        List<String> absolutePaths = listClassNames(innerDir);
        List<String> classNames = new ArrayList<>(absolutePaths.size());

        for (String name : absolutePaths) {
            String subPath = name.substring(name.indexOf(innerPath));
            if (subPath.contains("\\")) {
                subPath = subPath.replace("\\", ".");
            } else {
                subPath = subPath.replace("/", ".");
            }
            classNames.add(subPath);
        }
        return classNames;
    }

    private List<String> listClassNames(Path dir) {
        List<String> classNames = new ArrayList<>();
        File[] files = dir.toAbsolutePath().toFile().listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String cname = file.getAbsolutePath();
                classNames.add(cname.substring(0, cname.lastIndexOf(".")));
            } else {
                classNames.addAll(listClassNames(file.toPath()));
            }
        }
        return classNames;
    }

    /**
     * 校验加密类里的加密方法
     *
     * @param classLoader
     * @param classes
     * @return 返回未成功加密的方法
     * @throws ClassNotFoundException
     */
    private Set<Method> validateTranslateMethods(URLClassLoader classLoader, Set<String> classes)
        throws ClassNotFoundException {
        Set<Method> unTranslateMethod = new HashSet<>();
        for (String cname : classes) {
            Class clazz = classLoader.loadClass(cname);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                // 不是接口的default方法
                if (clazz.isInterface() && !method.isDefault()) {
                    continue;
                }
                // 非lambda方法
                if (!method.getName().contains("lambda$")
                    // 非匿名函数
                    && !method.getName().contains("access$")
                    && !Modifier.isNative(method.getModifiers())) {
                    unTranslateMethod.add(method);
                }
            }
        }
        return unTranslateMethod;
    }

    /**
     * @param classLoader
     * @param classes
     * @return 不该加密而加密的方法
     * @throws ClassNotFoundException
     */
    private Set<Method> validateKeepMethods(URLClassLoader classLoader, Set<String> classes)
        throws ClassNotFoundException {
        Set<Method> translateMethod = new HashSet<>();
        for (String cname : classes) {
            Class clazz = classLoader.loadClass(cname);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (Modifier.isNative(method.getModifiers())) {
                    translateMethod.add(method);
                }
            }
        }
        return translateMethod;
    }

    /**
     * 校验翻译方法, 方法定义参数后解析太过繁琐, 建议省去方法参数, 同时重要方法不要重载
     *
     * @param classLoader
     * @param methodNames            加密方法名称
     * @param translateMethodNames   keepClasses 里加密的方法
     * @param keepMethodNames        保留方法名称
     * @param unTranslateMethodNames 该加密而未加密的方法
     * @throws ClassNotFoundException
     */
    private void validateTranslateMethods(URLClassLoader classLoader,
        Set<String> methodNames, Set<String> translateMethodNames,
        Set<String> keepMethodNames, Set<String> unTranslateMethodNames) throws ClassNotFoundException {

        // 遍历 keepClasses 里加密的方法
        for (String name : translateMethodNames) {
            // 确保 keepsClasses里的加密方法都是 translateMethods 里定义的
            if (!methodNames.remove(name)) {
                throw new IllegalStateException(
                    String.format("Method : %s should not be translated, but actually yes.", name));
            }
        }
        // 校验剩下的不是 keepClasses 里的加密方法
        if (!methodNames.isEmpty()) {
            for (String methodName : methodNames) {
                validateTranslateMethod(classLoader, methodName, true);
            }
        }

        for (String name : unTranslateMethodNames) {
            // 确保 translateClasses 里的未加密方法都是 keepMethods 里定义的
            if (!keepMethodNames.remove(name)) {
                throw new IllegalStateException(
                    String.format("Method : %s should be translated, but actually not.", name));
            }
        }

        // 校验剩下的不是 keepClasses 里的加密方法
        if (!keepMethodNames.isEmpty()) {
            for (String methodName : keepMethodNames) {
                validateTranslateMethod(classLoader, methodName, false);
            }
        }
    }

    private void validateTranslateMethod(URLClassLoader classLoader, String fullMethodName, boolean translated)
        throws ClassNotFoundException {

        String className = fullMethodName.substring(0, fullMethodName.lastIndexOf("."));
        Class clazz = classLoader.loadClass(className);

        for (Method method : clazz.getDeclaredMethods()) {
            // 定位方法
            if (method.getName().equals(fullMethodName.substring(fullMethodName.lastIndexOf(".") + 1))) {
                // 该加密而未加密
                if (translated && !Modifier.isNative(method.getModifiers())) {
                    throw new IllegalStateException(
                        String.format("Method : %s should be translated, but actually not.", fullMethodName));
                }
                // 不该加密而加密
                if (!translated && Modifier.isNative(method.getModifiers())) {
                    throw new IllegalStateException(
                        String.format("Method : %s should not be translated, but actually yes.", fullMethodName));
                }
            }
        }
    }

    /**
     * 拷贝SO和JAR 到Resources目录
     */
    private void copyOutputToResources(Path path, List<File> files) throws IOException {
        // 创建扩折清空目录
        if(!path.toFile().exists()){
            Files.createDirectory(path);
        }else{
            removeFilesInDir(path.toAbsolutePath().toString(),null);
        }
        List<File> copyFiles = new ArrayList<>(files.size());
        for (File file : files) {
            if (file.getName().endsWith("jar") || file.getName().endsWith("so")) {
                copyFiles.add(file);
            }
        }
        for(File file : copyFiles){
            Path target = Paths.get(path.toAbsolutePath().toString(),file.getName());
            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void main(String[] args) throws Exception {
        IpGeoPackageMojo mojo = new IpGeoPackageMojo();
        mojo.j2cConfigDir = "D:\\apps\\ip-explorer\\ip-geo-package-plugin\\j2c";
        mojo.groupId = "com.alibaba.sec";
        mojo.artifactId = "ip-geo-fastclient";
        mojo.version = "1.0.0";
        //mojo.validateJava2CEncryption(Arrays.asList(
        //    new File("D:\\apps\\ip-explorer\\ip-geo-package-plugin\\j2c\\outIpClient\\ip-geo-fastclient-1.0.0.jar")));
        mojo.execute();
    }



}
