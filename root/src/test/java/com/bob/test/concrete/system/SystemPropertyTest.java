package com.bob.test.concrete.system;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统属性获取测试
 *
 * @author wb-jjb318191
 * @create 2018-02-09 9:24
 */
public class SystemPropertyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemPropertyTest.class);

    @Test
    public void testGetSystemEnvs() {
        Map<String, String> properties = System.getenv();
        for (Entry<String, String> entry : properties.entrySet()) {
            System.out.println(String.format("%s = %s", entry.getKey(), entry.getValue()));
        }
    }

    @Test
    public void testGetSystemEnv() {
        System.out.println(String.format(("OS = %s"), System.getenv("OS")));
    }

    @Test
    public void testGetSystemProperties() {
        Properties properties = System.getProperties();
        for (String name : properties.stringPropertyNames()) {
            System.out.println(name + String.format(" = %s", properties.get(name)));
        }
    }

    @Test
    public void testGetSystemProperty() {
        System.out.println(String.format("java.specification.version = %s", System.getProperty("java.specification.version")));
    }

    @Test
    public void testSetSystemProperty() {
        System.setProperty("java.specification.version", "51");
        testGetSystemProperty();
    }

}
