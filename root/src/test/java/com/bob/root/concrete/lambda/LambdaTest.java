/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.root.concrete.lambda;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.bob.root.utils.model.RootUser;
import org.junit.Test;

/**
 * @since 2017年3月3日 上午11:02:16
 * @version $Id$LambdaTest
 * @author JiangJibo
 *
 */
public class LambdaTest {

    private String info = "lanboal";

    @Test
    public void test1() {
        File file = new File("C:/Users/dell-7359/Desktop/log4j.properties");
        // 当参数只有一个时，外部括号可省去
        FileFilter java = f -> f.getName().endsWith("*.java");
        java.accept(file);
        new Thread(() -> {
            System.out.println(java.accept(file));
        }).start();
    }

    @Test
    public void test2() {
        LambdaInterface lambda = name -> name + info;
        System.out.println(lambda.getString("111"));
    }

    @Test
    public void compare() {

        // 如果函数体有多行，则用{}括起来，同时显式地return
        Comparator<String> c1 = ((o1, o2) -> {
            int num = compareString(o1, o2);
            return num - 1;
        });

        // 如果函数体只有一行,则可以省去{}及return还有;
        Comparator<String> c2 = (o1, o2) -> o1.length() > o2.length() ? 1 : -1;

        new Thread(() -> {
            System.out.println("......");
        }).start();

        System.out.println(c1.compare("aa", "bbb"));

        System.out.println(c2.compare("aa", "bbb"));
    }

    private int compareString(String o1, String o2) {
        return o1.length() > o2.length() ? 1 : -1;
    }

    @Test
    public void testComparator() {
        // 参数的类型可由编译器根据上下文推导而得出，所以可以不写参数类型
        System.out.println(compareStringByComparator("aaa", "bbb", (o1, o2) -> o1.length() > o2.length() ? 1 : -1));

    }

    private int compareStringByComparator(String o1, String o2, Comparator<String> c) {
        return c.compare(o1, o2);
    }

    public Runnable load() {
        return () -> {
            System.out.println("你好啊");
        };
    }

    @Test
    public void test() {
        List<RootUser> stus = new ArrayList<RootUser>();

        // 总数1行
        Collections.sort(stus, (s1, s2) -> s1.getName().compareTo(s2.getName()));

        // 总数3行
        Collections.sort(stus, (s1, s2) -> {
            return s1.getName().compareTo(s2.getName());
        });

        // 总数7行
        Collections.sort(stus, new Comparator<RootUser>() {

            @Override
            public int compare(RootUser o1, RootUser o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

}
