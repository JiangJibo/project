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
    public void testPrintHashCode(){
        System.out.println("tpoic#tag".hashCode());
        System.out.println(new HexComputeTest().hashCode());
    }

}
