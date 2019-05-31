package com.bob.root.concrete.binary;

import org.junit.Test;

/**
 * 十六进制位运算测试
 *
 * @author wb-jjb318191
 * @create 2018-02-26 10:52
 */
public class HexComputeTest {

    @Test
    public void test0() {
        // 0x1 代表16进制下的1，也就是16的0次方*1,  << >>代表向左或右移动

        // | ：位运算,或,当相应的位上有一个是1时,结果位上就是1
        //  0100
        //  0010
        //  0110  (结果)
        System.out.println(0x1 << 2 /*0100*/ | 0x1 << 1 /*0010*/) /*0110*/;

        // & ：位运算,与,只有两个数的相同位上同为1,结果位上才为1
        //  0101
        //  0011
        //  0001  (结果)
        System.out.println(0x5 /*0101*/ & 0x3 /*0011*/) /*0001*/;
    }

    @Test
    public void test1() {
        System.out.println(0x1 << 0);
        System.out.println((0x1 << 2 | 0x1 << 1) & 0x1 << 0);
        System.out.println(((0x1 << 2 | 0x1 << 1) & 0x1 << 0) == 0x1 << 0);
    }

    @Test
    public void testPrintHashCode() {
        System.out.println("tpoic#tag".hashCode());
        System.out.println(new HexComputeTest().hashCode());
    }

    @Test
    public void test16Radio() {
        // 0111 1111
        System.out.println(0x7F);
        // - 1000 0000
        System.out.println((~0x7F));
        System.out.println("##########################");
        int i = 56;
        System.out.println(i & ~0x7F);
        System.out.println(i & 0x7F);
        System.out.println((i & 0x7F) | 0x80);
        System.out.println("##########################");
        i = 129;
        System.out.println(i & ~0x7F);
        System.out.println(i & 0x7F);
        System.out.println((i & 0x7F) | 0x80);
        System.out.println("##########################");
        i = 257;
        // 1 0000 0001 & 1000 0000
        System.out.println(i & ~0x7F);
        System.out.println(i & 0x7F);
        System.out.println((i & 0x7F) | 0x80);
    }

    @Test
    public void testPrint(){
        // 1 0000 0001 & 0111 1111 = 0000 0001
        // 0000 0001 | 1000 0000 =
        print(10000);
        // 1111 1111
        // 11

    }

    private void print(int i){
        while ((i & ~0x7F) != 0) {
            // 将i的比8位高的数据写入, 同时最高位指定为1,表示后一个字节也是当前数据的
            // 比如 129 & 0x7F = 1, 257 & 0x7F = 1,
            // 0000 0000 | 0000 0000 | 0000 0001 | 0000 0001 & 0111 1111 = 0000 0000 | 0000 0000 | 0000 0000 | 0000 0001
            // 0000 0000 | 0000 0000 | 0000 0010 | 0000 0001 & 0111 1111 = 0000 0000 | 0000 0000 | 0000 0010 | 0000 0001
            System.out.println(i & 0x7f);
            System.out.println((byte)(i & 0x7f));
            System.out.println((i & 0x7f) | 0x80);
            System.out.println((byte)((i & 0x7f) | 0x80));
            // 然后将i向右移动7位,也就是抹去低7位
            i >>>= 7;
        }
        System.out.println(i);
    }

    /**
     * 测试将一个int拆分成4个字节
     */
    @Test
    public void testIntToByte(){
        int offset = 12562;
        // [0000 0000]
        System.out.println((byte)(offset >>> 24));
        // [0000 0000]
        System.out.println((byte)(offset >>> 16));
        // [0011 0001]
        System.out.println((byte)(offset >>> 8));
        // [0001 0010]
        System.out.println((byte)offset);
        //  [0000 0000 0000 0000 0011 0001 0001 0010] = 12562
    }

}
