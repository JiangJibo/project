package com.bob.test.concrete.binary;

import org.junit.Test;

/**
 * 二进制运算测试
 *
 * @author wb-jjb318191
 * @create 2018-02-26 10:52
 */
public class BinaryComputeTest {

    @Test
    public void test0() {
        System.out.println(0x1 << 2 /*0100*/ | 0x1 << 1 /*0010*/) /*0110*/;
    }

    @Test
    public void test1() {
        System.out.println(0x1 << 0);
        System.out.println((0x1 << 2 | 0x1 << 1) & 0x1 << 0);
        System.out.println(((0x1 << 2 | 0x1 << 1) & 0x1 << 0) == 0x1 << 0);
    }

}
