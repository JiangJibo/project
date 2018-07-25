package com.bob.root.concrete.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * 零拷贝测试
 *
 * @author wb-jjb318191
 * @create 2018-07-25 13:20
 */
public class ZeroCopyTest {

    private static final String file_path = "C:\\Users\\wb-jjb318191\\Desktop\\ediary.edf";

    private static final String copy_path = "C:\\Users\\wb-jjb318191\\Desktop\\zero_copy.edf";

    @Test
    public void testZeroCopy() throws IOException {
        FileInputStream inputStream = new FileInputStream(file_path);
        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = new FileOutputStream(copy_path).getChannel();
        long t1 = System.currentTimeMillis();
        inputChannel.transferTo(0, inputStream.available(), outputChannel);
        long t2 = System.currentTimeMillis();
        System.out.println("拷贝耗时:[" + (t2 - t1) + "]毫秒");
    }

    @Test
    public void testNormalIOCopy() throws IOException {
        long t1 = System.currentTimeMillis();
        IOUtils.copy(new FileInputStream(file_path), new FileOutputStream(copy_path));
        long t2 = System.currentTimeMillis();
        System.out.println("拷贝耗时:[" + (t2 - t1) + "]毫秒");
    }

}
