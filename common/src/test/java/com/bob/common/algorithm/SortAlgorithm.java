package com.bob.common.algorithm;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSON;

/**
 * 冒泡排序
 *
 * @author wb-jjb318191
 * @create 2020-06-16 9:32
 */
public class SortAlgorithm {

    public static void main(String[] args) {
        int[] ints1 = new int[] {1, 5, 2, 7, 8, 3, 4};
        int[] ints2 = new int[] {6, 5, 3, 4, 9, 10, 16};
        //bubbleSort(ints1);
        //insertionSort(ints2);
        //mergeSort(ints1, ints2);

        int[][] ints = new int[][] {ints1, ints2};
        mergeSort(ints);

        int[] arr = {10,7,2,4,7,62,3,4,2,1,8,9,19};
        quickSort(arr, 0, arr.length-1);
    }

    /**
     * 冒泡排序
     *
     * @param ints
     */
    public static void bubbleSort(int[] ints) {
        for (int i = 0; i < ints.length; i++) {
            boolean exchanged = false;
            int max = ints[i];
            for (int j = i + 1; j < ints.length; j++) {
                int x = ints[j];
                if (max > x) {
                    ints[j - 1] = x;
                    ints[j] = max;
                    exchanged = true;
                } else {
                    max = x;
                }
            }
            if (exchanged == false) {
                System.out.println("近运行了" + (i + 1) + "此排序");
                break;
            }

        }
        System.out.println(JSON.toJSONString(ints));
    }

    /**
     * 插入排序
     *
     * @param a 数组
     */
    public static void insertionSort(int[] a) {
        for (int i = 1; i < a.length; ++i) {
            int value = a[i];
            int j = i - 1;
            // 查找插入的位置
            for (; j >= 0; --j) {
                if (a[j] > value) {
                    a[j + 1] = a[j];  // 数据移动
                } else {
                    break;
                }
            }
            a[j + 1] = value; // 插入数据
        }
        System.out.println(JSON.toJSONString(a));
    }

    /**
     * 归并排序
     *
     * @param ints1
     * @param ints2
     */
    public static void mergeSort(int[] ints1, int[] ints2) {
        int[] ints = new int[ints1.length + ints2.length];
        int i = 0, j = 0, index = 0;
        do {
            if (i >= ints1.length) {
                ints[index++] = ints2[j];
                j++;
            } else if (j >= ints2.length) {
                ints[index++] = ints2[i];
                i++;
            } else {
                if (ints1[i] <= ints2[j]) {
                    ints[index++] = ints1[i];
                    i++;
                } else {
                    ints[index++] = ints2[j];
                    j++;
                }
            }
        } while (i < ints1.length || j < ints2.length);
        System.out.println(JSON.toJSONString(ints));
    }

    /**
     * 归并排序,合并有序数组
     *
     * @param ints
     */
    public static void mergeSort(int[][] ints) {
        for (int[] anInt : ints) {
            insertionSort(anInt);
        }

        long totalLength = Arrays.stream(ints).flatMap((Function<int[], Stream<?>>)ints1 -> Arrays.stream(ints1)
            .boxed()).count();

        int[] array = new int[(int)totalLength];

        int index = 0;
        int[] indexes = new int[ints.length];

        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < indexes.length; i++) {
            map.put(ints[i][0], i);
        }
        do {
            Map.Entry<Integer, Integer> entry = map.pollFirstEntry();
            if (entry == null) {
                break;
            }
            array[index++] = entry.getKey();
            // 当前数组的序号
            int x = entry.getValue();

            int order = indexes[x];

            if (order == ints[x].length - 1) {
                continue;
            }

            int key = ints[x][++order];
            if (map.containsKey(key)) {

            }

            map.put(ints[x][++order], x);
            indexes[x]++;
        } while (true);
        System.out.println(JSON.toJSONString(array));
    }

    /**
     * 快速排序
     *
     * @param arr
     * @param low
     * @param high
     */
    public static void quickSort(int[] arr, int low, int high) {
        int i, j, temp, t;
        if (low > high) {
            return;
        }
        i = low;
        j = high;
        //temp就是基准位
        temp = arr[low];

        while (i < j) {
            //先看右边，依次往左递减
            while (temp <= arr[j] && i < j) {
                j--;
            }
            //再看左边，依次往右递增
            while (temp >= arr[i] && i < j) {
                i++;
            }
            //如果满足条件则交换
            if (i < j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        //最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        //递归调用左半数组
        quickSort(arr, low, j - 1);
        //递归调用右半数组
        quickSort(arr, j + 1, high);
    }

}
