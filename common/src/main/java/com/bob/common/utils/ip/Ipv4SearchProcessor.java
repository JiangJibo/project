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
    private int[] ipStartOrder = new int[IP_FIRST_SEGMENT_SIZE];

    /**
     * ipStartOrder[128.100] = 1000 : 表示以128.100开头第一个IP段的序号是1000
     */
    private int[] ipEndOrder = new int[IP_FIRST_SEGMENT_SIZE];

    /**
     * 每条ip段的结束ip long值
     * ipStartOrder[128.100] = 1200 : 表示以128.100开始最后一个IP段的序号是1200
     */
    private long[] endIpLong;

    /**
     * addressIndex[N] = 1000, 表示第N个ip段的数据是 {@link #addressArray} 的地N个位置的字符串
     */
    private int[] addressIndex;

    private String[] addressArray;

    /**
     * 元数据的字节长度
     */
    private static final int META_INFO_BYTE_LENGTH = 1024;

    private static final int IP_FIRST_SEGMENT_SIZE = 256 * 256;

    private Ipv4SearchProcessor(String filePath) {
        this.init(filePath);
    }

    private boolean init(String filePath) {
        byte[] data;
        try {
            data = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new IllegalArgumentException(
                String.format("IPV4 data file path not applicable, path:{%s}", filePath));
        }
        for (int i = 0; i < IP_FIRST_SEGMENT_SIZE; i++) {
            int k = META_INFO_BYTE_LENGTH + 8 + i * 8;
            ipStartOrder[i] = readInt(data, k);
            ipEndOrder[i] = readInt(data, k + 4);
        }
        // 前4字节存储ip条数
        int recordSize = readInt(data, META_INFO_BYTE_LENGTH);
        endIpLong = new long[recordSize];
        addressIndex = new int[recordSize];
        addressArray = new String[readInt(data, META_INFO_BYTE_LENGTH + 4)];

        int index = 0;
        Map<String, Integer> addressMappings = new HashMap<>();
        for (int i = 0; i < recordSize; i++) {
            // 8 + 256*8 +
            int pos = META_INFO_BYTE_LENGTH + 8 + IP_FIRST_SEGMENT_SIZE * 8 + (i * 9);
            // 前4字节存储结束的ip值
            this.endIpLong[i] = readVLong4(data, pos);
            int offset = readInt(data, pos + 4);
            int length = data[pos + 8] & 0xff;
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
        return true;
    }

    public static Ipv4SearchProcessor newInstance(String path) {
        return new Ipv4SearchProcessor(path);
    }

    public String search(String ip) {
        // 计算ip前缀的int值
        int firstDotIndex = ip.indexOf(".");
        int firstSegment = calculateIpSegmentInt(ip, 0, firstDotIndex - 1);

        int secondDotIndex = ip.indexOf(".", firstDotIndex + 1);
        int secondSegment = calculateIpSegmentInt(ip, firstDotIndex + 1, secondDotIndex - 1);

        int segmengIndex = (firstSegment << 8) + secondSegment;

        int start = ipStartOrder[segmengIndex], end = ipEndOrder[segmengIndex];
        int cur = start == end ? end : binarySearch(start, end, calculateIpLong(ip, segmengIndex, secondDotIndex + 1));
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
        int order = 0;
        while (low <= high) {
            int mid = (low + high) >> 1;
            if (endIpLong[mid] >= k) {
                order = mid;
                if (mid == 0) {
                    break;
                }
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return order;
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
    private long calculateIpLong(String ip, long result, int dot) {
        int num;
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
            if (dotIndex < 0) {
                return result;
            }
        } while (true);
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
