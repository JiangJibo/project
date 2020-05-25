package com.bob.root.concrete.cpu;

import org.junit.Before;
import org.junit.Test;

/**
 * @author JiangJibo
 * @create 2020-05-12 22:45
 */
public class CpuCacheLineTest {

    int[] ints;

    @Before
    public void init() {
        ints = new int[1000 * 1000 * 10];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = i;
        }
    }

    @Test
    public void testCacheLine0() {
        long t1 = System.nanoTime();
        for (int i = 0; i < 1000 * 100; i++) {
            int j = 0;
            while (j < ints.length) {
                int x = ints[j];
                int y = ints[j + 15];
                j += 64;
            }
        }
        long t2 = System.nanoTime();
        System.out.println((t2 - t1) / 1000 / 1000);
    }

    @Test
    public void testCacheLine1() {
        long t1 = System.nanoTime();
        for (int i = 0; i < 1000 * 100; i++) {
            int j = 10;
            while (j < ints.length) {
                int x = ints[j];
                int y = ints[j + 53];
                j += 64;
            }
        }
        long t2 = System.nanoTime();
        System.out.println((t2 - t1) / 1000 / 1000);
    }



}
