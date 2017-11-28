package com.bob.test.concrete.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import com.bob.test.config.BaseControllerTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * CountdownLatch测试用例
 *
 * @author wb-jjb318191
 * @create 2017-11-28 9:50
 */
public class CountDownLatchTest extends BaseControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountDownLatchTest.class);

    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(2);

    @Autowired
    public ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    protected void init() {

    }

    /**
     * @throws InterruptedException
     */
    @Test
    public void testCountDownLatch() throws InterruptedException {

        Callable<String> callable1 = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(System.currentTimeMillis());
                Thread.sleep(1000);
            }
            //表示当前线程的任务执行结束,将计数器减一。当计数器为0是,就会从await()方法中醒来
            COUNT_DOWN_LATCH.countDown();
            return "success";
        };

        Callable<String> callable2 = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("第" + i + "次输出");
                Thread.sleep(1000);
            }
            COUNT_DOWN_LATCH.countDown();
            return "success";
        };

        threadPoolTaskExecutor.submit(callable1);
        threadPoolTaskExecutor.submit(callable2);

        COUNT_DOWN_LATCH.await();
        System.out.println("CountDownLatch执行结束");
    }

}
