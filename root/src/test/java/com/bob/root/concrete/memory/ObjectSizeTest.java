package com.bob.root.concrete.memory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    public void testListByte(){
        List<Byte> bts = new ArrayList<>();
        System.out.println(ObjectSizeCalculator.getObjectSize(bts));
        bts.add((byte)'1');
        System.out.println(ObjectSizeCalculator.getObjectSize(bts));
    }

    @Test
    public void testUserSize(){
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

    @Data
    public static class User{
        private int id;
        private String name;
        private long telephone;
        private Integer uid;
    }

}
