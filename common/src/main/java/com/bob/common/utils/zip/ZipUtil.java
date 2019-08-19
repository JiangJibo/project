package com.bob.common.utils.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * zip工具类
 *
 * @author wb-jjb318191
 * @create 2018-07-12 16:25
 */
public class ZipUtil {

    /**
     * ZIP文件上传的目录
     */
    private static final String ZIP_SAVE_PARENT_DIRECTORY_KEY = "";

    /**
     * 解压Zip文件
     *
     * @param rawData 上传的文件流
     * @return 解压后的目录
     * @throws IOException
     */
    public String unZip(InputStream rawData) throws Exception {
        File zip = null;
        ZipFile zipFile = null;
        try {
            zip = saveZip(rawData);
            String unZipDir = createUnZipDir(zip);
            zipFile = new ZipFile(zip, "gbk");
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                unZip(zipFile, (ZipEntry)entries.nextElement(), unZipDir);
            }
            return unZipDir;
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
                if (zip != null) {
                    zip.delete();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * 穷举指定目录下的指定类型的文件
     *
     * @param dirPath 指定目录
     * @param suffix  文件后缀
     * @return
     */
    public List<File> listSpecifiedFiles(String dirPath, String suffix) {
        Assert.hasText(dirPath, "穷举目录不能为空");
        Assert.state(!dirPath.contains("."), "穷举目录不能是一个文件,或者说不能包含\".\"");
        List<File> all = new ArrayList<File>();
        introspectFiles(dirPath, all);
        if (!StringUtils.hasText(suffix) || "*".equals(suffix)) {
            return all;
        }
        List<File> result = new ArrayList<File>(all.size());
        for (File file : all) {
            if (file.getName().endsWith(suffix)) {
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
    public void removeDir(String path) {
        Assert.hasText(path, "待移除的目录名称不能为空");
        File dir = new File(path);
        if (!dir.exists()) {
            return;
        }
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                removeDir(file.getAbsolutePath());
            }
            dir.delete();
        } else {
            dir.delete();
        }

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
    private void unZip(ZipFile zipFile, ZipEntry entry, String parentPath) throws IOException {
        String fileName = entry.getName();
        File file = new File(parentPath + "/" + fileName);
        // 如果当前元素是目录
        if (entry.isDirectory()) {
            if (!file.exists()) {
                file.mkdirs();
            }
            return;
        }
        // 当前元素是文件,但可能上级目录不存在
        if (fileName.contains("/")) {
            String subPath = parentPath + "/" + entry.getName().substring(0, entry.getName().lastIndexOf("/"));
            // 如果上级目录不存在
            File parent = new File(subPath);
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
        file.createNewFile();
        FileOutputStream fos = null;
        try {
            IOUtils.copy(zipFile.getInputStream(entry), fos = new FileOutputStream(file));
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 保存Zip数据
     *
     * @param data
     * @return 返回保存后的文件
     */
    private File saveZip(InputStream data) throws IOException {
        File file = generateSaveFile();
        FileOutputStream fos = null;
        try {
            IOUtils.copy(data, fos = new FileOutputStream(file));
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return file;
    }

    /**
     * 生成保存zip文件的名称
     *
     * @return
     */
    private File generateSaveFile() throws IOException {
        File file = new File(getSaveParentDirectory() + "/" + System.currentTimeMillis() + ".zip");
        if (file.createNewFile()) {
            return file;
        } else {
            return generateSaveFile();
        }
    }

    /**
     * 创建解压目录
     *
     * @param zipFile
     * @return
     */
    private String createUnZipDir(File zipFile) {
        String unZipDir = zipFile.getAbsolutePath().substring(0, zipFile.getAbsolutePath().lastIndexOf("."));
        File dir = new File(unZipDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return unZipDir;
    }

    /**
     * 获取ZIP保存的父目录
     *
     * @return
     */
    private String getSaveParentDirectory() {
        return "C:\\Users\\wb-jjb318191\\Desktop";
    }

    public static void main(String[] args) throws Exception {
        ZipUtil util = new ZipUtil();
        String path = "C:\\Users\\wb-jjb318191\\Desktop\\新建文件夹.zip";
        String dir = util.unZip(new FileInputStream(path));
        System.out.println(dir);
        List<File> files = util.listSpecifiedFiles(dir, "txt");
        System.out.println(files.get(0).getAbsolutePath());
        util.removeDir(dir);
    }

}
