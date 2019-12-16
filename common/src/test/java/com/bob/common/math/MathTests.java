package com.bob.common.math;

import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2019-12-16 15:46
 */
public class MathTests {

    /**
     * {@link Math#log(double)} 方法默认用e作为底数, e的值是2.71828
     * e的多少次方 = 参数值
     */
    @Test
    public void testLog(){
        float e = (float)2.71828;
        System.out.println(Math.log(e));
        System.out.println(Math.log10(12));
    }

    /**
     * 求根号
     */
    @Test
    public void testSqrt(){
        System.out.println(Math.sqrt(4));
    }

}
