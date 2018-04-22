package com.bob.root.concrete.concurrent;

import org.junit.Test;

/**
 * @author Administrator
 * @create 2018-04-22 14:42
 */
public class SynchronizedTest {

    private int num = -1;

    private volatile int vo = -1;

    @Test
    public void testSync() {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> num++).start();
            System.out.println(String.format("i=%d,num=%d", i, num));
            if (num == 1000) {
                break;
            }
        }
    }

    @Test
    public void testVo() {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> ++vo).start();
            System.out.println(String.format("i=%d,vo=%d", i, vo));
            if (num == 1000) {
                break;
            }
        }
    }

}
