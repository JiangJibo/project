package com.bob.common.utils.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.springframework.util.StringUtils;

/**
 * @author josnow
 * @version 1.0.0
 * @date 2017年5月24日 下午3:12:31
 * @desc zip工具集成原CompressUtil方法，增加了对内存文件和流文件的压缩以避免产生临时文件
 */
public class Zip4jUtils {

    /**
     * @param fileName 文件名
     * @param data     文件数据
     * @param password 密码
     * @desc 将内存文件写入zip内。注意：最后必须调用closeZipOutputStream关闭输出流，或者手动关闭
     * @auth josnow
     * @date 2017年5月24日 下午5:23:02
     */
    public static void addFileToZip(String fileName, byte[] data, String password, ZipOutputStream zipOutputStream)
        throws ZipException, IOException {

        if (StringUtils.isEmpty(fileName) || data == null || data.length == 0 || zipOutputStream == null) {
            throw new ZipException(new StringBuilder("参数异常,fileName=").append(fileName).append(",data=").append(data)
                .append(",zipOutputStream=").append(zipOutputStream).toString());
        }

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别

        zipParameters.setFileNameInZip(fileName);

        if (StringUtils.hasText(password)) {
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            zipParameters.setPassword(password.toCharArray());
        }

        // 源文件是否为外部流，true表示使用内存文件而非本地存储文件
        zipParameters.setSourceExternalStream(true);

        zipOutputStream.putNextEntry(null, zipParameters);
        zipOutputStream.write(data);
        zipOutputStream.closeEntry();
    }

    /**
     * @param fileName 文件名
     * @param data     文件数据
     * @desc 将内存文件写入zip内。注意：最后必须调用closeZipOutputStream关闭输出流，或者手动关闭
     * @auth josnow
     * @date 2017年5月24日 下午5:46:02
     */
    public static void addFileToZip(String fileName, byte[] data, ZipOutputStream zipOutputStream)
        throws ZipException, IOException {
        addFileToZip(fileName, data, null, zipOutputStream);
    }

    /**
     * @param zipParameters   zip参数
     * @param data            文件数据
     * @param zipOutputStream 输出流
     * @desc 将内存文件写入zip内。注意：最后必须调用closeZipOutputStream关闭输出流，或者手动关闭
     * @auth josnow
     * @date 2017年5月25日 上午11:08:56
     */
    public static void addFileToZip(ZipParameters zipParameters, byte[] data, ZipOutputStream zipOutputStream)
        throws ZipException, IOException {

        if (zipParameters == null || data == null || data.length == 0 || zipOutputStream == null) {
            throw new ZipException(new StringBuilder("参数异常,zipParameters=").append(zipParameters).append(",data=")
                .append(data).append(",zipOutputStream=").append(zipOutputStream).toString());
        }
        zipOutputStream.putNextEntry(null, zipParameters);
        zipOutputStream.write(data);
        zipOutputStream.closeEntry();
    }

    /**
     * @param zipOutputStream 输出流
     * @desc 关闭流
     * @auth josnow
     * @date 2017年5月25日 上午11:16:01
     */
    public static void closeZipOutputStream(ZipOutputStream zipOutputStream) throws IOException, ZipException {
        if (zipOutputStream == null) {
            return;
        }
        zipOutputStream.finish();
        zipOutputStream.close();
    }



}