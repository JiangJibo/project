package com.bob.root.concrete.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2018-04-23 13:31
 */
public class AQSTest {

    private ReentrantLock lock = new ReentrantLock();
    private AtomicInteger flag = new AtomicInteger(0);

    @Test
    public void testLock() throws InterruptedException {
        lock.lock();
        try {
            debugLockBlocking();

            long endTime = System.currentTimeMillis() + 20000;
            do {
                if (System.currentTimeMillis() > endTime) {
                    break;
                }
            } while (!flag.compareAndSet(1, 0));

            System.out.println("获得锁的线程已经执行完毕");
        } finally {
            lock.unlock();
        }
    }

    private void debugLockBlocking() {
        Runnable runnable = () -> {
            lock.lock();
            try {
                System.out.println(String.format("线程id:[%s]正常获取到锁", Thread.currentThread().getId()));
            } catch (Exception e) {

            } finally {
                lock.unlock();
            }
        };
        new Thread(runnable).start();
    }

}
