package com.bob.common.utils.ip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.springframework.util.StopWatch;

/**
 * @author wb-jjb318191
 * @create 2020-03-18 18:21
 */
@SuppressWarnings("Duplicates")
public class Ipv4SearcherV3 {

    /**
     * ipStartOrder[128] = 1000 : 表示以128开始第一个IP段的序号是1000
     */
    private int[] ipStartOrder = new int[256];

    /**
     * ipStartOrder[128] = 1200 : 表示以128开始最后一个IP段的序号是1200
     */
    private int[] ipEndOrder = new int[256];

    /**
     * 每条ip段的结束ip long值
     * endIpLong[N] = 1256624556, 表示第N个ip段的结束ip的long值是 1256624556
     */
    private long[] endIpLong;

    /**
     * addressIndex[N] = 1000, 表示第N个ip段的数据是 {@link #addressArray} 的地N个位置的字符串
     */
    private int[] addressIndex;

    private String[] addressArray = new String[20 * 1000];

    private static Ipv4SearcherV3 instance = null;

    private Ipv4SearcherV3() {
        Path path = Paths.get("C:\\Users\\wb-jjb318191\\Desktop\\全球旗舰版-202002-636871\\qqzeng-ip-3.0-ultimate.dat");

        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int k = 0; k < 256; k++) {
            int i = k * 8 + 4;
            ipStartOrder[k] = readInt(data, i);
            ipEndOrder[k] = readInt(data, i);
        }
        // 钱4字节存储ip条数
        int recordSize = readInt(data, 0);
        endIpLong = new long[recordSize];
        addressIndex = new int[recordSize];

        int index = 0;
        Map<String, Integer> addressMappings = new HashMap<>();

        for (int i = 0; i < recordSize; i++) {
            // 4 + 256*8 = 2052
            int p = 2052 + (i * 8);
            // 前4字节存储结束的ip值
            this.endIpLong[i] = readVLong4(data, p);
            int offset = readVInt3(data, p + 4);
            int length = data[p + 7] & 0xff;
            // 将所有字符串都取出来, 每个字符串都缓存好
            String address = new String(Arrays.copyOfRange(data, offset, (offset + length)));
            // 缓存字符串
            if (!addressMappings.containsKey(address)) {
                addressArray[index] = address;
                addressMappings.put(address, index);
                index++;
            }
            addressIndex[i] = index - 1;
        }

    }

    public static synchronized Ipv4SearcherV3 getInstance() {
        if (instance == null) {
            instance = new Ipv4SearcherV3();
        }
        return instance;
    }

    public String search(String ip) {

        String[] ips = ip.split("\\.");
        int pref = Integer.valueOf(ips[0]);
        long val = ipToLong(ip);
        int low = ipStartOrder[pref], high = ipEndOrder[pref];
        long cur = low == high ? low : binarySearch(low, high, val);
        return addressArray[addressIndex[(int)cur]];

    }

    private int binarySearch(int low, int high, long k) {
        int m = 0;
        while (low <= high) {
            int mid = (low + high) / 2;
            long endipNum = endIpLong[mid];
            if (endipNum >= k) {
                m = mid;
                if (mid == 0) {
                    break;
                }
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return m;
    }

    /**
     * 读取4个字节的凑成Long
     *
     * @param data
     * @param p
     * @return
     */
    private long readVLong4(byte[] data, int p) {
        return (readVInt3(data, p)) | ((data[p + 3] << 24) & 0xFF000000L);
    }

    /**
     * 读取4个字节合成Int
     *
     * @param data
     * @param p
     * @return
     */
    private int readInt(byte[] data, int p) {
        return (int)(readVLong4(data, p));
    }

    /**
     * 读取3个字节凑成Int
     *
     * @param data
     * @param p
     * @return
     */
    private int readVInt3(byte[] data, int p) {
        return (int)((data[p++] & 0xFFL) | ((data[p++] << 8) & 0xFF00L) | ((data[p++] << 16) & 0xFF0000L));
    }

    private long ipToLong(String ip) {
        long result = 0;
        String[] d = ip.split("\\.");
        for (String b : d) {
            result <<= 8;
            result |= Long.parseLong(b) & 0xff;
        }
        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        Ipv4SearcherV3 finder = Ipv4SearcherV3.getInstance();
        StopWatch watch = new StopWatch();
        watch.start();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000 * 1000 * 1000; i++) {
                finder.search("8.14.224.189");
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000 * 1000 * 1000; i++) {
                finder.search("126.14.224.11");
            }
        });
        //thread1.start();
        //thread2.start();

        watch.stop();
        System.out.println(watch.getLastTaskTimeMillis());
        //System.out.println(ip);
        //System.out.println(result);
        //System.gc();
        System.out.println(finder.search("126.14.224.11"));
        System.out.println(ObjectSizeCalculator.getObjectSize(finder));


        /*
         * 1.197.224.9 亚洲|中国|河南|周口|商水|电信|411623|China|CN|114.60604|33.53912
         */
    }

}
