package com.bob.root.concrete.memory;

import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import javafx.util.Pair;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;
import sun.misc.Unsafe;

/**
 * @author wb-jjb318191
 * @create 2020-05-21 9:25
 */
public class ObjectSizeTest {

    @Test
    @SneakyThrows
    public void StringTest() {
        String s = "abcdefgh";
        System.out.println(ObjectSizeCalculator.getObjectSize(s));  // 56

        List<String> as = new ArrayList<>(1);
        as.add(s);
        System.out.println(ObjectSizeCalculator.getObjectSize(as)); // 104

        String[] sa = new String[1];
        System.out.println(ObjectSizeCalculator.getObjectSize(sa));  // 24

        sa[0] = s;
        System.out.println(ObjectSizeCalculator.getObjectSize(sa));  // 80

        byte[] bts = s.getBytes("UTF-8");
        System.out.println(ObjectSizeCalculator.getObjectSize(bts)); // 24

    }

    @Test
    @SneakyThrows
    public void testListByte() {
        List<Byte> bts = new ArrayList<>();
        System.out.println(ObjectSizeCalculator.getObjectSize(bts));
        bts.add((byte)'1');
        System.out.println(ObjectSizeCalculator.getObjectSize(bts));
    }

    @Test
    public void testUserSize() {
        User user = new User();
        System.out.println(ObjectSizeCalculator.getObjectSize(user));   // 32
        user.setId(1);
        System.out.println(ObjectSizeCalculator.getObjectSize(user));   // 32
        user.setUid(1);
        System.out.println(ObjectSizeCalculator.getObjectSize(user));   // 48
        user.setName("a");
        System.out.println(ObjectSizeCalculator.getObjectSize(user));   // 96
    }

    private static Unsafe unsafe;

    static {
        try {
            Field getUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            getUnsafe.setAccessible(true);
            unsafe = (Unsafe)getUnsafe.get(null);
        } catch (Exception ex) { throw new Error(ex); }
    }

    @Test
    @SneakyThrows
    public void testFiledOffset() {
        long telephoneOffset = unsafe.objectFieldOffset(String.class.getDeclaredField("value"));
        System.out.println(telephoneOffset);
    }

    @Test
    public void testObjectSize() {
        System.out.println("############### 优化前 ###############");

        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        System.out.println(String.format("HashMap形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(map)));                //264

        map = ImmutableMap.of("key", "value");
        System.out.println(String.format("ImmutableMap形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(map)));                // 144

        Entry<String, String> entry = new SimpleEntry<>("key", "value");
        System.out.println(String.format("Map.Entry形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(entry)));              // 128

        Pair<String, String> kv = new Pair<>("key", "value");
        System.out.println(String.format("Pair形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(kv)));                 // 128

        List<String> stringList = Arrays.asList("key", "value");
        System.out.println(String.format("List形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(stringList)));         //152

        String[] strings = new String[2];
        strings[0] = "key";
        strings[1] = "value";
        System.out.println(String.format("数组形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(strings)));            //128

        System.out.println("############### 优化后 ###############");

        char[] chars = new char[3 + 1 + 5];
        System.arraycopy("key".toCharArray(), 0, chars, 0, 3);
        chars[3] = '0';
        System.arraycopy("value".toCharArray(), 0, chars, 4, 5);
        System.out.println(String.format("char数组形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(chars)));              //40

        byte[] bytes = new byte[3 + 1 + 5];
        System.arraycopy("key".getBytes(), 0, bytes, 0, 3);
        chars[3] = '0';
        System.arraycopy("value".getBytes(), 0, bytes, 4, 5);
        System.out.println(String.format("byte数组形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(bytes)));              //32
    }

    /**
     * 批量存储手机号码
     */
    @Test
    public void testSavePhones() {
        // 使用字符串形式存储
        String[] stringPhones = new String[] {"01234567890", "12345678901", "23456789012", "34567890123", "45678901234"};
        System.out.println(String.format("字符串数组形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(stringPhones)));   // 360

        // 使用long存储
        long[] longPhones = new long[] {1234567890L, 12345678901L, 23456789012L, 34567890123L, 45678901234L};
        System.out.println(String.format("long数组形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(longPhones)));     // 56

        // 使用压缩过的字节存储, 每个手机号用5个字节存储
        byte[] bytePhones = new byte[5 * 5];
        for (int i = 0; i < longPhones.length; i++) {
            byte[] bts = toBytes(longPhones[i]);
            System.arraycopy(bts, 3, bytePhones, i * 5, 5);
        }
        System.out.println(String.format("压缩过后字节数组形式存储，内存占用%d字节", ObjectSizeCalculator.getObjectSize(bytePhones)));     // 48
    }

    //每次截取8位，然后左移8,
    private byte[] toBytes(long val) {
        byte[] b = new byte[8];
        for (int i = 7; i > 0; i--) {
            //强制转型，后留下长整形的低8位
            b[i] = (byte)val;
            val >>>= 8;
        }
        b[0] = (byte)val;
        return b;
    }

    @Test
    @SneakyThrows
    public void testStringToByteLength() {
        String string = "今天:2020-05-25";
        System.out.println(string.getBytes("utf-8").length);        // 17
        System.out.println(string.getBytes("GBK").length);          // 15
        System.out.println(string.getBytes("GB18030").length);      // 15
    }

    @Data
    public static class User {

        private int id;
        private String name;
        private long telephone;
        private Integer uid;
    }

    @Data
    public static class Model {

        private String abc;
    }

    @Test
    public void testDataSize() {
        Model model = new Model();
        model.setAbc("a|b|c");
        System.out.println(String.format("合并属性，内存占用%d字节",
            ObjectSizeCalculator.getObjectSize(model)));   // 168
    }

    /**
     * 存储多选框的选项
     */
    @Test
    public void testSaveCheckBoxSelectedData() {

        Set<Integer> options = ImmutableSet.of(1, 2, 3, 5);
        System.out.println(String.format("Set<Integer> 对象尺寸:%d 字节",
            ObjectSizeCalculator.getObjectSize(options)));  // 176

        System.out.println(String.format("JSON 形式字符串大小：%d 字节",
            ObjectSizeCalculator.getObjectSize(JSON.toJSONString(options))));  // 64

        int option = (1 << 0) + (1 << 1) + (1 << 2) + (1 << 4);  // "10111" 23
        System.out.println(String.format("标志位表示: %s, 占用4字节", Integer.toBinaryString(option)));

        int order = 3;  // 第3+1=4位
        int selected = option >> order & 1;
        System.out.println(String.format("第%d位是否勾选:%s", order + 1, selected == 1));

    }

}
