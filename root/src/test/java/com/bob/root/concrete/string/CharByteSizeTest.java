package com.bob.root.concrete.string;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.junit.Test;

/**
 * @author JiangJibo
 * @create 2020-03-14 12:56
 */
public class CharByteSizeTest {

    @Test
    public void test1(){
        String ss = "a我";
        System.out.println(ss.charAt(1));
        System.out.println(ObjectSizeCalculator.getObjectSize(ss.charAt(0)));
        System.out.println(ss.charAt(1));
        System.out.println(ss.getBytes().length);
    }


}
