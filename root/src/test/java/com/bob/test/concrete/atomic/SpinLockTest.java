package com.bob.test.concrete.atomic;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 自旋锁测试
 *
 * @author wb-jjb318191
 * @create 2018-02-26 15:07
 */
public class SpinLockTest {

    private AtomicBoolean atomicBoolean = new AtomicBoolean(true);

    private ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    @Before
    public void initBefore(){
        executor.initialize();
    }

    /**
     * 测试自旋锁,通过{@link sun.misc.Unsafe}实现硬件层面上的自旋锁
     */
    @Test
    public void testSpin() {

        Callable<String> task = () -> {
            Thread.sleep(3000);
            atomicBoolean.compareAndSet(true, false);
            return "success";
        };
        executor.submit(task);

        boolean flag;
        do {  //通过AtomicBoolean实现自旋锁,自旋直到当前线程获得锁
            flag = this.atomicBoolean.compareAndSet(false, true);
        }
        while (!flag);
        System.out.println("获得了锁");
    }

}
