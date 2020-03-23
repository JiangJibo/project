package com.bob.common.utils.ip;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wb-jjb318191
 * @create 2020-03-18 18:21
 */
public class Ipv4SearchProcessor {

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

    private String[] addressArray;

    private static Ipv4SearchProcessor instance = null;

    private static final int IP_FIRST_SEGMENT_SIZE = 256;

    private Ipv4SearchProcessor(String filePath) {
        byte[] data;
        try {
            data = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new IllegalArgumentException(
                String.format("IPV4 data file path not applicable, path:{%s}", filePath));
        }
        for (int k = 0; k < IP_FIRST_SEGMENT_SIZE; k++) {
            int i = k * 8 + 8;
            ipStartOrder[k] = readInt(data, i);
            ipEndOrder[k] = readInt(data, i + 4);
        }
        // 前4字节存储ip条数
        int recordSize = readInt(data, 0);
        endIpLong = new long[recordSize];
        addressIndex = new int[recordSize];
        addressArray = new String[readInt(data, 4)];

        int index = 0;
        Map<String, Integer> addressMappings = new HashMap<>();
        for (int i = 0; i < recordSize; i++) {
            // 8 + 256*8 +
            int p = 8 + IP_FIRST_SEGMENT_SIZE * 8 + (i * 9);
            // 前4字节存储结束的ip值
            this.endIpLong[i] = readVLong4(data, p);
            int offset = readInt(data, p + 4);
            int length = data[p + 8] & 0xff;
            // 将所有字符串都取出来, 每个字符串都缓存好
            String address = new String(Arrays.copyOfRange(data, offset, (offset + length)));
            // 缓存字符串, 如果是一个新的字符串
            if (!addressMappings.containsKey(address)) {
                addressArray[index] = address;
                addressMappings.put(address, index);
                addressIndex[i] = index++;
            } else {
                addressIndex[i] = addressMappings.get(address);
            }
        }
    }

    public static synchronized Ipv4SearchProcessor getInstance(String path) {
        if (instance == null) {
            instance = new Ipv4SearchProcessor(path);
        }
        return instance;
    }

    public String search(String ip) {
        // 计算ip前缀的int值
        int num = calculateIpSegmentInt(ip, 0, ip.indexOf(".") - 1);
        int start = ipStartOrder[num], end = ipEndOrder[num];
        int cur = start == end ? start : binarySearch(start, end, calculateIpLong(ip));
        return addressArray[addressIndex[cur]];
    }

    /**
     * 定位ip序号
     *
     * @param low  ip段的起始序号
     * @param high ip段的结束序号
     * @param k
     * @return
     */
    private int binarySearch(int low, int high, long k) {
        int m = 0;
        while (low <= high) {
            int mid = (low + high) >> 1;
            if (endIpLong[mid] >= k) {
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
     * 读取4个字节合成Int
     *
     * @param data
     * @param p
     * @return
     */
    private int readInt(byte[] data, int p) {
        return (int)readVLong4(data, p);
    }

    /**
     * 读取4个字节的凑成Long
     *
     * @param data
     * @param p
     * @return
     */
    private long readVLong4(byte[] data, int p) {
        return ((data[p++] << 24) & 0xFF000000L) | readVInt3(data, p);
    }

    /**
     * 读取3个字节凑成Int
     *
     * @param data
     * @param p
     * @return
     */
    private int readVInt3(byte[] data, int p) {
        return (int)(((data[p++] << 16) & 0xFF0000L) | ((data[p++] << 8) & 0xFF00L) | (data[p++] & 0xFFL));
    }

    /**
     * 计算ip的long值
     *
     * @param ip
     * @return
     */
    private long calculateIpLong(String ip) {
        long result = 0;
        int num, dot = 0, i = 0;
        do {
            int dotIndex = ip.indexOf(".", dot) - 1;
            if (dotIndex >= 0) {
                num = calculateIpSegmentInt(ip, dot, dotIndex);
            }
            // 最后一段
            else {
                num = calculateIpSegmentInt(ip, ip.lastIndexOf(".") + 1, ip.length() - 1);
            }
            result <<= 8;
            result |= num & 0xff;
            dot = dotIndex + 2;
            if (i++ == 3) {
                break;
            }
        } while (true);
        return result;
    }

    /**
     * 计算IP段的int值
     *
     * @param ip
     * @param startIndex
     * @param endIndex
     * @return
     */
    private int calculateIpSegmentInt(String ip, int startIndex, int endIndex) {
        int num = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            int radix = i == endIndex ? 1 : i == endIndex - 1 ? 10 : 100;
            num += radix * (ip.charAt(i) - 48);
        }
        return num;
    }

}
