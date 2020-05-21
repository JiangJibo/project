package com.bob.root.concrete.memory;

import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.SneakyThrows;
import org.junit.Test;

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

}
