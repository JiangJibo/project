package com.bob.root.concrete.clazz;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author wb-jjb318191
 * @create 2020-05-14 9:31
 */
public class URLClassLoaderTest {

    private boolean success = false;

    private static final String PG_FILE_NAME = "ip-geo-fastclient-pg.jar";

    @Test
    @SneakyThrows
    public void testIpSearch() {
        testIPv4Search();
        testIPv6Search();
        success = true;
    }

    @After
    @SneakyThrows
    public void afterTest() {
        // 测试成功了拷贝proguard生成的jar文件
        if (success) {
            File jar = getFastClientJarFile(PG_FILE_NAME);
            Path copyPath = buildOutputCopyPath();
            Files.copy(jar.toPath(), copyPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @SneakyThrows
    public void testIPv4Search() {
        String dataFilePath = getSourceFile("ipv4-geo.dex");
        String lsnFilePath = getSourceFile("license-ipv4.lic");
        File jar = getFastClientJarFile(PG_FILE_NAME);

        URLClassLoader classLoader = buildClassLoader(jar);
        Object geoConf = instanceGeoConfiguration(classLoader, lsnFilePath, dataFilePath);
        Object fastClient = instanceFastIPGeoClient(classLoader, geoConf);

        String result = invokeSearch(fastClient, "221.206.131.10");
    }

    @SneakyThrows
    public void testIPv6Search() {
        String dataFilePath = getSourceFile("ipv6-geo.dex");
        String lsnFilePath = getSourceFile("license-ipv6.lic");
        File jar = getFastClientJarFile("ip-geo-fastclient-pg.jar");

        URLClassLoader classLoader = buildClassLoader(jar);
        Object geoConf = instanceGeoConfiguration(classLoader, lsnFilePath, dataFilePath);
        Object fastClient = instanceFastIPGeoClient(classLoader, geoConf);

        String result = invokeSearch(fastClient, "240e:00e0:4fc5:0000:0000:0000:0000:0001");
    }

    private URLClassLoader buildClassLoader(File file) throws MalformedURLException {
        return new URLClassLoader(new URL[] {new URL("file:" + file.getAbsolutePath())},
            Thread.currentThread().getContextClassLoader());
    }

    /**
     * 实例化配置类
     *
     * @param classLoader
     * @param lsnFilePath
     * @param dataFilePath
     * @return
     * @throws Exception
     */
    private Object instanceGeoConfiguration(URLClassLoader classLoader, String lsnFilePath, String dataFilePath)
        throws Exception {
        Class<?> geoConfClass = classLoader.loadClass("com.alibaba.sec.domain.FastGeoConf");
        Constructor constructor = geoConfClass.getConstructor();
        Object geoCon = constructor.newInstance();
        Method licenseSetterMethod = geoConfClass.getMethod("setLicenseFilePath", String.class);
        licenseSetterMethod.invoke(geoCon, lsnFilePath);
        Method datFileSetterMethod = geoConfClass.getMethod("setDataFilePath", String.class);
        datFileSetterMethod.invoke(geoCon, dataFilePath);
        return geoCon;
    }

    /**
     * 实例化FastClient
     *
     * @param classLoader
     * @param geoConf
     * @return
     * @throws Exception
     */
    private Object instanceFastIPGeoClient(URLClassLoader classLoader, Object geoConf) throws Exception {
        Class<?> geoConfClass = classLoader.loadClass("com.alibaba.sec.client.FastIPGeoClient");
        Constructor constructor = geoConfClass.getConstructor(geoConf.getClass());
        return constructor.newInstance(geoConf);
    }

    /**
     * 执行检索
     *
     * @param fastClient
     * @param ip
     * @return
     * @throws Exception
     */
    private String invokeSearch(Object fastClient, String ip) throws Exception {
        Method searchMethod = fastClient.getClass().getMethod("search", String.class);
        return (String)searchMethod.invoke(fastClient, ip);
    }

    private static String getSourceFile(String name) {
        Path root = Paths.get("").toAbsolutePath().toAbsolutePath().getParent();
        Path path = Paths.get(root.toString(), "ip-geo-fastclient", "src", "test", "resources", name);
        return path.toFile().getAbsolutePath();
    }

    private static File getFastClientJarFile(String fileName) {
        Path root = Paths.get("").toAbsolutePath().toAbsolutePath().getParent();
        Path path = Paths.get(root.toString(), "ip-geo-fastclient", "target", fileName);
        return path.toAbsolutePath().toFile();
    }

    private Path buildOutputCopyPath() {
        String currentPath = Paths.get("").toAbsolutePath().toString();
        String root = currentPath.substring(0, currentPath.lastIndexOf(File.separator));
        return Paths.get(root, "classes", "artifacts", "ip_geo_fastclient_pg_jar", PG_FILE_NAME);
    }

}
