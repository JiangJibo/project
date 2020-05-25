package com.bob.root.concrete.thread;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;

/**
 * 上下文切换测试
 */
public final class ContextSwitchTest {

    static final int RUNS = 3;
    static final int ITERATES = 1000000;

    static AtomicReference turn = new AtomicReference();

    static final class WorkerThread extends Thread {

        volatile Thread other;
        volatile int nparks;

        public void run() {
            final AtomicReference t = turn;
            final Thread other = this.other;

            if (turn == null || other == null) {
                throw new NullPointerException();
            }

            int p = 0;
            for (int i = 0; i < ITERATES; ++i) {
                while (!t.compareAndSet(other, this)) {
                    LockSupport.park();
                    ++p;
                }
                LockSupport.unpark(other);
            }
            LockSupport.unpark(other);
            nparks = p;
            System.out.println("parks: " + p);

        }
    }

    static void test() throws Exception {
        WorkerThread a = new WorkerThread();
        WorkerThread b = new WorkerThread();
        a.other = b;
        b.other = a;
        turn.set(a);
        long startTime = System.nanoTime();
        a.start();
        b.start();
        a.join();
        b.join();
        long endTime = System.nanoTime();
        int parkNum = a.nparks + b.nparks;
        // 每次park需要多少纳秒
        System.out.println("Average time: " + ((endTime - startTime) / parkNum)
            + "ns");
    }

    @Test
    public void testMultiFor() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 1000; j++) {
                for (int k = 0; k < 10000; k++) {
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Time spent is " + (end - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 1000; j++) {
                for (int k = 0; k < 100; k++) {

                }
            }
        }
        end = System.currentTimeMillis();
        System.out.println("Time spent is " + (end - start) + "ms");
    }
}
