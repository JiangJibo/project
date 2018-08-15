package com.bob.web.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 克隆antx配置项,将去引入Spring容器中
 *
 * @author wb-jjb318191
 * @create 2018-05-18 15:08
 */
public class AntxConfigReplicator {

    private static final String ANTX_CONFIG_PATH = "antx.properties";
    private static final String ANTX_BACKUP_CONFIG_PATH = "web/src/main/resources/antx-backup.properties";

    public static void main(String[] args) throws IOException {
        Properties antxBackupProperties = new Properties();
        Properties antxProperties = new Properties();
        antxProperties.load(new FileInputStream(ANTX_CONFIG_PATH));
        for (String key : antxProperties.stringPropertyNames()) {
            antxBackupProperties.setProperty(key, "${" + key + "}");
        }
        antxBackupProperties.store(new FileOutputStream(ANTX_BACKUP_CONFIG_PATH), "Import antx.properties configuration into Spring context");

    }

}
