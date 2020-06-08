package com.bob.root.concrete.nio;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * 直接内存测试
 *
 * @author Administrator
 * @create 2018-07-24 20:51
 */
public class DirectBufferTest {

    @Test
    public void testDirectBuffer() {
        loopAllocateDirect();
    }

    private void allocateDirect() {
        ByteBuffer.allocateDirect(1024 * 1024 * 200); // 100MB
    }

    private void loopAllocateDirect() {
        for (int i = 0; i < 5000; i++) {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 100);  //100K
        }
    }

    private void loopAllocate() {
        for (int i = 0; i < 5000; i++) {
            ByteBuffer.allocate(1024 * 100);  //100K
        }
    }

    @Test
    @SneakyThrows
    public void testAllocDirectMemory() {
        List<String> ips = Arrays.asList("aaaaa", "bbbbb", "ccccc");
        byte[] bts = new byte[18];
        int index = 0;
        for (int i = 0; i < ips.size(); i++) {
            bts[index++] = 5;
            System.arraycopy(ips.get(i).getBytes(), 0, bts, index, 5);
            index += 5;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(18);
        byteBuffer.put(bts);
        byteBuffer.flip();

        byte bt = byteBuffer.get();
        byte[] str = new byte[bt];
        byteBuffer.get(str);
        System.out.println(new String(str));
    }

    @Test
    @SneakyThrows
    public void testDirectMemoryIterator() {
        List<String> ips = FileUtils.readLines(new File("C:\\Users\\wb-jjb318191\\Desktop\\ipv4-ip.txt"), "UTF-8");
        List<Byte> bytes = new ArrayList<>(ips.size() * 6);
        for (int i = 0; i < ips.size(); i++) {
            String ip = ips.get(i);
            bytes.add((byte)ip.length());
            listAddArray(bytes, ip.getBytes());
        }
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.size());
        for (Byte bt : bytes) {
            byteBuffer.put(bt);
        }
        byteBuffer.flip();
        byte[] str;
        System.gc();
        Thread.sleep(3000);
        long t1 = System.currentTimeMillis();
        do {
            byte bt = byteBuffer.get();
            str = new byte[bt];
            byteBuffer.get(str);
            String string = new String(str);
        } while (byteBuffer.hasRemaining());
        Thread.sleep(10000);
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
    }

    @Test
    @SneakyThrows
    public void testHeapMemoryIterator() {
        List<String> ips = FileUtils.readLines(new File("C:\\Users\\wb-jjb318191\\Desktop\\ipv4-ip.txt"), "UTF-8");
        List<Byte> bytes = new ArrayList<>(ips.size() * 6);
        for (int i = 0; i < ips.size(); i++) {
            String ip = ips.get(i);
            bytes.add((byte)ip.length());
            listAddArray(bytes, ip.getBytes());
        }
        byte[] byteBuffer = new byte[bytes.size()];
        int i = 0;
        for (Byte bt : bytes) {
            byteBuffer[i++] = bt;
        }

        int offset = 0;
        long t1 = System.currentTimeMillis();
        do {
            byte bt = byteBuffer[offset++];
            String string = new String(byteBuffer, offset, bt);
            offset += bt;
        } while (offset < byteBuffer.length);

        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
    }

    private void listAddArray(List<Byte> list, byte[] array) {
        for (byte t : array) {
            list.add(t);
        }
    }

}
