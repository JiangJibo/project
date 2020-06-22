package com.bob.common.skpilist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.util.StopWatch;

/**
 * @author wb-jjb318191
 * @create 2020-06-19 17:42
 */
public class ConcurrentSkipListMapTest {


    @Test
    @SneakyThrows
    public void testSkipListMap() {
        testConcurrentMap(new ConcurrentSkipListMap<Integer, Integer>());

    }

    @Test
    @SneakyThrows
    public void testConcurrentTreeMap() {
        testConcurrentMap(new TreeMap<Integer, Integer>());

    }

    @Test
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(5000);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.clear();
        System.out.println("");
    }


    @SneakyThrows
    public void testConcurrentMap(Map<Integer, Integer> map){
        CountDownLatch latch = new CountDownLatch(4);


        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000 * 1000; i++) {
                map.put(i, i);
            }

            map.forEach((integer, integer2) -> {
                int x = integer + integer2;
            });
            latch.countDown();
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 1000 * 1000; i < 2000 * 1000; i++) {
                map.put(i, i);
            }

            map.forEach((integer, integer2) -> {
                int x = integer + integer2;
            });
            latch.countDown();
        });
        Thread thread3 = new Thread(() -> {
            for (int i = 3000 * 1000; i < 4000 * 1000; i++) {
                map.put(i, i);
            }

            map.forEach((integer, integer2) -> {
                int x = integer + integer2;
            });
            latch.countDown();
        });
        Thread thread4 = new Thread(() -> {
            for (int i = 4000 * 1000; i < 5000 * 1000; i++) {
                map.put(i, i);
            }

            map.forEach((integer, integer2) -> {
                int x = integer + integer2;
            });
            latch.countDown();
        });
        StopWatch watch = new StopWatch();
        watch.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        latch.await();
        watch.stop();
        System.out.println(watch.getLastTaskTimeMillis());
    }

}
