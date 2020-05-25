package com.bob.root.concrete.memory;

import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
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
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        System.out.println(ObjectSizeCalculator.getObjectSize(map));                //264

        map = ImmutableMap.of("key", "value");
        System.out.println(ObjectSizeCalculator.getObjectSize(map));                // 144

        Entry<String, String> entry = new SimpleEntry<>("key", "value");
        System.out.println(ObjectSizeCalculator.getObjectSize(entry));              // 128

        Pair<String, String> kv = new Pair<>("key", "value");
        System.out.println(ObjectSizeCalculator.getObjectSize(kv));                 // 128

        List<String> stringList = Arrays.asList("key", "value");
        System.out.println(ObjectSizeCalculator.getObjectSize(stringList));         //152

        String[] strings = new String[2];
        strings[0] = "key";
        strings[1] = "value";
        System.out.println(ObjectSizeCalculator.getObjectSize(strings));            //128

        char[] chars = new char[3 + 1 + 5];
        System.arraycopy("key".toCharArray(), 0, chars, 0, 3);
        chars[3] = '0';
        System.arraycopy("value".toCharArray(), 0, chars, 4, 5);
        System.out.println(ObjectSizeCalculator.getObjectSize(chars));              //40

        byte[] bytes = new byte[3 + 1 + 5];
        System.arraycopy("key".getBytes(), 0, bytes, 0, 3);
        chars[3] = '0';
        System.arraycopy("value".getBytes(), 0, bytes, 4, 5);
        System.out.println(ObjectSizeCalculator.getObjectSize(bytes));              //32
    }

    @Test
    public void testSavePhones() {
        // 使用字符串形式存储
        String[] stringPhones = new String[] {"01234567890", "12345678901", "23456789012", "34567890123", "45678901234"};
        System.out.println(ObjectSizeCalculator.getObjectSize(stringPhones));   // 360

        // 使用long存储
        long[] longPhones = new long[] {1234567890L, 12345678901L, 23456789012L, 34567890123L, 45678901234L};
        System.out.println(ObjectSizeCalculator.getObjectSize(longPhones));     // 56

        // 使用压缩过的字节存储
        byte[] bytePhones = new byte[5 * 5];
        for (int i = 0; i < longPhones.length; i++) {
            byte[] bts = toBytes(longPhones[i]);
            System.arraycopy(bts, 3, bytePhones, i * 5, 5);
        }
        System.out.println(ObjectSizeCalculator.getObjectSize(bytePhones));     // 48

        System.out.println(Integer.toBinaryString(65535 * 128));

    }

    //每次截取8位，然后左移8,
    public static byte[] toBytes(long val) {
        System.out.println("原来的长整形数据：" + val);
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

        private String a;
        private String b;
        private String c;
    }

    @Test
    public void testDataSize() {
        Model model = new Model();
        model.setA("a");
        model.setB("b");
        model.setC("c");
        System.out.println(ObjectSizeCalculator.getObjectSize(model));   // 168

        Map<String, String> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        map.put("c", "c");
        System.out.println(ObjectSizeCalculator.getObjectSize(map));   // 368
    }

}
