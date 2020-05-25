package com.bob.root.concrete.thread;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author JiangJibo
 * @create 2020-05-10 14:27
 */
public class LockBenchmark {

    public static void runIncrement() {
        long counter = 0;
        long max = 500000000L;
        long start = System.currentTimeMillis();
        while (counter < max) { counter++; }
        long end = System.currentTimeMillis();
        System.out.println("Time spent is " + (end - start) + "ms without lock");
    }

    public static void runIncrementWithLock() {
        Lock lock = new ReentrantLock();
        long counter = 0;
        long max = 500000000L;
        long start = System.currentTimeMillis();
        while (counter < max) {
            if (lock.tryLock()) {
                counter++;
                lock.unlock();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Time spent is " + (end - start) + "ms with lock");
    }

    public static void runIncrementAtomic() {
        AtomicLong counter = new AtomicLong(0);
        long max = 500000000L;
        long start = System.currentTimeMillis();
        while (counter.incrementAndGet() < max) { }
        long end = System.currentTimeMillis();
        System.out.println("Time spent is " + (end - start) + "ms with cas");
    }

    public static void main(String[] args) {
        runIncrement();
        runIncrementAtomic();
        runIncrementWithLock();
    }
}
