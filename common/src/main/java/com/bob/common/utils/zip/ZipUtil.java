package com.bob.common.utils.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
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

    // 压缩
    public static void zip(String zipFileName, String inputFile)
        throws Exception {
        zip(zipFileName, new FileInputStream(new File(inputFile)), inputFile.substring(inputFile.lastIndexOf(".")));
    }

    public static void zip(String zipFileName, InputStream is, String baseName)
        throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName, true));
        zip(out, is, baseName);
        out.close();
    }

    /**
     * 压缩内存中的多个数据
     *
     * @param zipFileName
     * @param iss
     * @throws Exception
     */
    @SuppressWarnings("Duplicates")
    public static void zip(String zipFileName, List<SimpleEntry<String, InputStream>> iss)
        throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName, true));
        for (SimpleEntry<String, InputStream> entry : iss) {
            String name = entry.getKey();
            InputStream is = entry.getValue();
            if (StringUtils.hasText(name) && is != null) {
                zip(out, is, name);
            }
        }
        out.close();
    }

    /**
     * 压缩内存中的多个文件
     *
     * @param iss
     * @return
     * @throws Exception
     */
    @SuppressWarnings("Duplicates")
    public static ZipOutputStream zip(List<SimpleEntry<String, InputStream>> iss)
        throws Exception {
        ZipOutputStream out = new ZipOutputStream(new ByteArrayOutputStream());
        for (SimpleEntry<String, InputStream> entry : iss) {
            String name = entry.getKey();
            InputStream is = entry.getValue();
            if (StringUtils.hasText(name) && is != null) {
                zip(out, is, name);
            }
        }
        return out;
    }

    /**
     * 打包内存中的数据,返回字节数组
     *
     * @param iss
     * @return
     * @throws Exception
     */
    public static byte[] zipToByteBuffer(List<SimpleEntry<String, InputStream>> iss)
        throws Exception {
        ZipOutputStream os = zip(iss);
        os.close();
        Field field0 = ReflectionUtils.findField(os.getClass(), "out");
        field0.setAccessible(true);
        // 通过反射获取内部数据
        ByteArrayOutputStream baos = (ByteArrayOutputStream)field0.get(os);
        Field field1 = ReflectionUtils.findField(baos.getClass(), "buf");
        field1.setAccessible(true);
        return (byte[])field1.get(baos);
    }

    private static void zip(ZipOutputStream out, InputStream in, String base)
        throws Exception {
        out.putNextEntry(new ZipEntry(base));
        IOUtils.copy(in, out);
        in.close();
    }

    public static void main(String[] args) throws Exception {
        List<InputStream> iss = Arrays.asList(new FileInputStream("C:\\Users\\wb-jjb318191\\Desktop\\all.txt"),
            new FileInputStream("C:\\Users\\wb-jjb318191\\Desktop\\circles"));
        //byte[] data = ZipUtil.zipToByteBuffer(iss, Arrays.asList("all.txt", "circles"));
        String zipPath = "C:\\Users\\wb-jjb318191\\Desktop\\compress.zip";
        //IOUtils.write(data, new FileOutputStream(zipPath));

    }

}
