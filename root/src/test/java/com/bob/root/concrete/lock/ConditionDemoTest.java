package com.bob.root.concrete.lock;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2018-10-15 19:17
 */
public class ConditionDemoTest {

    private Lock lock = new ReentrantLock();

    private Condition readCondition = lock.newCondition();

    private Condition writeCondition = lock.newCondition();

    private Queue<String> queue = new ArrayDeque();

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Test
    public void testCondition() throws InterruptedException {
        executorService.submit(this::read);
        executorService.submit(this::write);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void read() {
        lock.lock();
        try {
            while (true) {
                System.out.println("执行read方法");
                while (!queue.isEmpty()) {
                    queue.poll();
                }
                Thread.sleep(3000);
                System.out.println("read方法执行结束");
                writeCondition.signal();
                readCondition.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void write() {
        int i = 0;
        lock.lock();
        try {
            while (true) {
                System.out.println("执行write方法");
                while (queue.size() < 10) {
                    queue.add("" + i++);
                }
                Thread.sleep(3000);
                System.out.println("write方法执行结束");
                readCondition.signal();
                writeCondition.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
