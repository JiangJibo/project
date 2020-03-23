package com.bob.common.ip;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.bob.common.utils.ip.Ipv4SearchProcessor;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.StopWatch;

/**
 * @author wb-jjb318191
 * @create 2020-03-23 9:56
 */
public class Ipv4SearchProcessorTest {

    @Test
    public void invokeInThreads() throws InterruptedException, IOException {
        Ipv4SearchProcessor finder = Ipv4SearchProcessor.newInstance("C:\\Users\\wb-jjb318191\\Desktop\\ipv4-utf8-index.dat");
        List<String> ips = FileUtils.readLines(new File("C:\\Users\\wb-jjb318191\\Desktop\\ips.txt"), "UTF-8");
        StopWatch watch = new StopWatch();
        watch.start();

        CountDownLatch latch = new CountDownLatch(2);

        int size = ips.size();

        Thread thread1 = new Thread(() -> {
            int k = 0;
            for (int i = 0; i < 1000 * 1000 * 1000; i++) {
                finder.search(ips.get(k++));
                if (k == size) {
                    k = 0;
                }
            }
            latch.countDown();
        });
        Thread thread2 = new Thread(() -> {
            int k = 0;
            for (int i = 0; i < 1000 * 1000 * 1000; i++) {
                finder.search(ips.get(k++));
                if (k == size) {
                    k = 0;
                }
            }
            latch.countDown();
        });
        Thread thread3 = new Thread(() -> {
            int k = 0;
            for (int i = 0; i < 1000 * 1000 * 100; i++) {
                finder.search(ips.get(k++));
                if (k == size) {
                    k = 0;
                }
            }
            latch.countDown();
        });
        Thread thread4 = new Thread(() -> {
            int k = 0;
            for (int i = 0; i < 1000 * 1000 * 100; i++) {
                //String ip = ips.get(k++);
                finder.search(ips.get(k++));
                if (k == size) {
                    k = 0;
                }
            }
            latch.countDown();
        });
        //thread1.start();
        //thread2.start();
        thread3.start();
        thread4.start();
        latch.await();

        watch.stop();
        System.out.println(watch.getLastTaskTimeMillis());
        //System.out.println(ip);
        //System.out.println(result);
        //System.gc();
        System.out.println(finder.search("1.0.4.0"));
        System.out.println(ObjectSizeCalculator.getObjectSize(finder));
    }

    @Test
    public void invokeInOneThread() throws IOException {
        Ipv4SearchProcessor finder = Ipv4SearchProcessor.newInstance("C:\\Users\\wb-jjb318191\\Desktop\\ipv4-utf8-index.dat");
        //ObjectSizeCalculator.getObjectSize(finder);
        //System.out.println(finder.search("186.0.42.66"));
        List<String> ips = FileUtils.readLines(new File("C:\\Users\\wb-jjb318191\\Desktop\\ips.txt"), "UTF-8");
        StopWatch watch = new StopWatch();
        watch.start();
        int k = 0;
        for (int i = 0; i < 1000 * 1000 * 1000; i++) {
            finder.search(ips.get(k++));
            if (k == ips.size()) {
                k = 0;
            }
        }
        watch.stop();
        System.out.println(watch.getLastTaskTimeMillis());
    }

}
