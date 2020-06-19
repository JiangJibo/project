package com.bob.common;

/**
 * 快速排序
 *
 * @author JiangJibo
 * @create 2020-06-19 7:49
 */
public class QuickSort {

    public static void quickSort(int[] ints) {
        int length = ints.length;
        int pivot = length >> 1;

        int middle = ints[pivot];
        for (int i = 0; i < pivot; i++) {
            int x = ints[i];
            if (x < middle) {
                for (int j = pivot - 1; j > 0; j--) {
                    int a = ints[j];
                    if (x >= a) {

                    }
                }
            } else if (x > middle) {
                for (int j = pivot + 1; j < ints.length; j++) {

                }
            } else {

            }
        }

    }

}
