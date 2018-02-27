package com.bob.test.concrete.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NIO测试类
 *
 * @author wb-jjb318191
 * @create 2017-09-05 16:32
 */
public class NioTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NioTest.class);

    /**
     * @throws IOException
     */
    @Test
    public void testNio() throws IOException {
        FileInputStream fin = new FileInputStream(new File(
            "D:\\theme.xml"));
        FileChannel fc = fin.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        fc.read(byteBuffer);
        fc.close();
        byteBuffer.flip();//读写转换
    }

    /**
     * @throws IOException
     */
    @Test
    public void testCopyFileWithNio() throws IOException {
        copyFileWithNio("D:\\theme.xml", "D:\\theme-copy.xml");
    }

    /**
     *
     */
    @Test
    public void showBufferDetail() {
        ByteBuffer buffer = ByteBuffer.allocate(15); // 15个字节大小的缓冲区
        System.out.println("limit=" + buffer.limit() + " capacity=" + buffer.capacity() + " position=" + buffer.position());
        for (int i = 0; i < 10; i++) {
            // 存入10个字节数据
            buffer.put((byte)i);
        }
        System.out.println("limit=" + buffer.limit() + " capacity=" + buffer.capacity() + " position=" + buffer.position());
        //flip()函数的作用是将写模式转变为读模式，
        // 即将写模式下的Buffer中内容的最后位置变为读模式下的limit位置，作为读越界位置，同时将当前读位置置为0，表示转换后重头开始读，同时再消除写模式下的mark标记
        buffer.flip();
        System.out.println("limit=" + buffer.limit() + " capacity=" + buffer.capacity() + " position=" + buffer.position());
        for (int i = 0; i < 10; i++) {
            System.out.print(buffer.get());
        }
        System.out.println();
        System.out.println("limit=" + buffer.limit() + " capacity=" + buffer.capacity() + " position=" + buffer.position());
        buffer.flip();
        System.out.println("limit=" + buffer.limit() + " capacity=" + buffer.capacity() + " position=" + buffer.position());
    }

    /**
     * @throws IOException
     */
    @Test
    public void mappingFileIntoMemory() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("D:\\theme.xml", "rw");
        FileChannel fc = raf.getChannel();         // 将文件映射到内存中
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
        while (mbb.hasRemaining()) {
            System.out.print((char)mbb.get());
        }
        mbb.put(0, (byte)98); // 修改文件
        raf.close();
    }

    /**
     * 通过NIO拷贝文件
     *
     * @param resource
     * @param destination
     * @throws IOException
     */
    private static void copyFileWithNio(String resource, String destination) throws IOException {
        FileInputStream fis = new FileInputStream(resource);
        FileOutputStream fos = new FileOutputStream(destination);
        FileChannel readChannel = fis.getChannel(); // 读文件通道
        FileChannel writeChannel = fos.getChannel(); // 写文件通道
        ByteBuffer buffer = ByteBuffer.allocate(1024); // 读入数据缓存
        while (true) {
            buffer.clear();
            int len = readChannel.read(buffer); // 写入数据
            if (len == -1) {
                break; // 读取完毕
            }
            buffer.flip();  //从写入模式转换为读取模式
            writeChannel.write(buffer); // 写入文件
        }
        readChannel.close();
        writeChannel.close();
    }

}
