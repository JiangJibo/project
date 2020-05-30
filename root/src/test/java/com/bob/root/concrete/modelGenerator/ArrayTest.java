package com.bob.root.concrete.modelGenerator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author JiangJibo
 * @create 2020-05-27 19:45
 */
public class ArrayTest {

    private int m = 1;

    private int[] array = new int[] {1, 3, 4, 7, 2, 9};

    @Test
    public void test() {

        List<String> list = new ArrayList<>();

        int length = 9 + m + 1;

        int[] x = new int[length];
        int[] y = new int[length];

        for (int i = 0; i < array.length; i++) {
            int c = array[i];
            if (c - m >= 0) {
                x[c - m] = c - m;
            }
            y[c + m] = c + m;
        }

        for (int i = 0; i < array.length; i++) {
            int c = array[i];

            if (x[c] == c) {
                list.add((c) + "," + (c + m));
            }

            if (y[c] == c && c - m > 0 && x[c - m] == 0) {
                list.add((c - m) + "," + c);
            }

        }

        System.out.println(list.toString());

    }

}
