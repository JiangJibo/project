package com.bob.test.concrete.nio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import org.junit.Test;

/**
 * RocketMQ的数据文件借助了MappedByteBuffer,实现大文件的高性能读写
 *
 * https://www.jianshu.com/p/f90866dcbffc (MappedByteBuffer详解博客)
 *
 * 从代码层面上看，从硬盘上将文件读入内存，都要经过文件系统进行数据拷贝，并且数据拷贝操作是由文件系统和硬件驱动实现的，理论上来说，拷贝数据的效率是一样的。
 * 但是通过内存映射的方法访问硬盘上的文件，效率要比read和write系统调用高，这是为什么？
 *
 * read()是系统调用，首先将文件从硬盘拷贝到内核空间的一个缓冲区，再将这些数据拷贝到用户空间，实际上进行了两次数据拷贝；
 * map()也是系统调用，但没有进行间接的数据拷贝，当缺页中断发生时，直接将文件从硬盘拷贝到用户空间，只进行了一次数据拷贝。
 * 所以，采用内存映射的读写效率要比传统的read/write性能高。
 *
 * @author wb-jjb318191
 * @create 2018-02-27 9:52
 */
public class MappedByteBufferTest {

    @Test
    public void testMappedByteBuffer() {
        File file = new File("D://data.txt");
        long len = file.length();
        byte[] ds = new byte[(int)len];

        try {
            //FileChannel提供了map方法把文件映射到虚拟内存，通常情况可以映射整个文件，如果文件比较大，可以进行分段映射。
            MappedByteBuffer mappedByteBuffer = new RandomAccessFile(file, "r").getChannel()
                .map(FileChannel.MapMode.READ_ONLY, 0, len);

            //MapMode.READ_ONLY：只读，试图修改得到的缓冲区将导致抛出异常。
            //MapMode.READ_WRITE：读 / 写，对得到的缓冲区的更改最终将写入文件；但该更改对映射到同一文件的其他程序不一定是可见的。
            //MapMode.PRIVATE：私用，可读可写, 但是修改的内容不会写入文件，只是buffer自身的改变，这种能力称之为”copy on write”。

            for (int offset = 0; offset < len; offset++) {
                byte b = mappedByteBuffer.get();
                ds[offset] = b;
            }

            Scanner scan = new Scanner(new ByteArrayInputStream(ds)).useDelimiter(" ");
            while (scan.hasNext()) {
                System.out.print(scan.next() + " ");
            }

        } catch (IOException e) {}
    }

}
