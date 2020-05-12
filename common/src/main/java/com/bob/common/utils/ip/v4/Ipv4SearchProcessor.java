package com.bob.common.utils.ip.v4;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import com.bob.common.utils.ip.IpGeoMetaInfo;
import org.apache.commons.io.FileUtils;

import static com.bob.common.utils.ip.DigestUtils.rawToJSON;
import static com.bob.common.utils.ip.IpGeoConstants.CONTENT_CHAR_SET;

/**
 * @author wb-jjb318191
 * @create 2020-03-18 18:21
 */
public class Ipv4SearchProcessor {

    /**
     * 元数据的字节长度
     */
    public static final int META_INFO_BYTE_LENGTH = 1024;

    private static final int IP_FIRST_SEGMENT_SIZE = 256 * 256;

    /**
     * ipBlockStart[128] = 1000 : 表示以128开始第一个IP段的序号是1000
     */
    private int[] ipBlockStart;

    /**
     * ipBlockStart[128.100] = 1000 : 表示以128.100开头第一个IP段的序号是1000
     */
    private int[] ipBlockEnd;

    /**
     * 每条ip段的结束ip的后两个字节
     */
    private byte[] endIpBytes;

    /**
     * contentIndexes[N] = 1000, 表示第N个ip段的数据是 {@link #contentArray} 的地N个位置的字符串
     */
    private byte[] contentIndexes;

    /**
     * 内容数组
     */
    private String[] contentArray;

    public boolean init(String filePath, IpGeoMetaInfo metaInfo) throws Exception {

        byte[] data = FileUtils.readFileToByteArray(new File(filePath));
        ipBlockStart = new int[IP_FIRST_SEGMENT_SIZE];
        ipBlockEnd = new int[IP_FIRST_SEGMENT_SIZE];

        for (int i = 0; i < IP_FIRST_SEGMENT_SIZE; i++) {
            int k = META_INFO_BYTE_LENGTH + 8 + i * 8;
            ipBlockStart[i] = readInt(data, k);
            ipBlockEnd[i] = readInt(data, k + 4);
        }

        // 前4字节存储ip条数
        int recordSize = readInt(data, META_INFO_BYTE_LENGTH);
        endIpBytes = new byte[recordSize << 1];
        contentIndexes = new byte[recordSize * 3];
        // 有多少条唯一性的内容
        contentArray = new String[readInt(data, META_INFO_BYTE_LENGTH + 4)];

        int index = 0;
        // int形式的内容位置
        int[] contentIndex = new int[recordSize];
        // 原始内容与处理过的内容间的映射
        Map<String, String> contentMappings = new HashMap<>();
        Map<String, Integer> contentIndexMappings = new HashMap<>();
        for (int i = 0; i < recordSize; i++) {
            // 8 + 256*8 +
            int pos = META_INFO_BYTE_LENGTH + 8 + IP_FIRST_SEGMENT_SIZE * 8 + (i * 9);
            // 前4字节存储结束的ip值
            this.endIpBytes[2 * i] = data[pos];
            this.endIpBytes[2 * i + 1] = data[pos + 1];
            int offset = readInt(data, pos + 2);
            int length = readVInt3(data, pos + 6);
            // 将所有字符串都取出来, 每个字符串都缓存好
            String rawContent = new String(Arrays.copyOfRange(data, offset, offset + length), CONTENT_CHAR_SET);
            // 对原始内容做处理, 不重复处理
            String content;
            if (contentMappings.containsKey(rawContent)) {
                content = contentMappings.get(rawContent);
            } else {
                content = rawToJSON(rawContent, Arrays.asList(metaInfo.getStoredProperties()),
                    new HashSet<>(Arrays.asList(metaInfo.getLoadProperties())));
                contentMappings.put(rawContent, content);
            }
            // 缓存字符串, 如果是一个新的字符串
            if (!contentIndexMappings.containsKey(content)) {
                contentArray[index] = content;
                contentIndexMappings.put(content, index);
                contentIndex[i] = index++;
            } else {
                contentIndex[i] = contentIndexMappings.get(content);
            }
        }
        // 将内容位置的int数组转换成字节数组,节省一个字节
        for (int i = 0; i < contentIndex.length; i++) {
            writeVInt3(contentIndexes, 3 * i, contentIndex[i]);
        }

        return true;
    }

    private void writeVInt3(byte[] data, int offset, int i) {
        data[offset++] = (byte)(i >> 16);
        data[offset++] = (byte)(i >> 8);
        data[offset] = (byte)i;
    }

    /**
     * 解码内容, 生成json
     *
     * @param rawContent
     * @param properties     所有属性
     * @param loadProperties 加载属性
     * @return
     */
    private String decodeContent(String rawContent, List<String> properties, Set<String> loadProperties) {
        String[] splits = rawContent.split("\\|", properties.size());
        JSONObject jsonObject = new JSONObject();
        // 键值对按顺序一一匹配
        for (int i = 0; i < splits.length; i++) {
            String value = splits[i].trim().length() > 0 ? splits[i].trim() : null;
            // 指定加载的属性
            if (loadProperties.contains(properties.get(i))) {
                jsonObject.put(properties.get(i), value);
            }
        }
        return jsonObject.toJSONString();
    }

    public String search(String ip) {
        // 计算ip前缀的int值
        int firstDotIndex = ip.indexOf(".");
        int firstSegmentInt = calculateIpSegmentInt(ip, 0, firstDotIndex - 1);

        // 计算ip第二段的int值
        int secondDotIndex = ip.indexOf(".", firstDotIndex + 1);
        int secondSegmentInt = calculateIpSegmentInt(ip, firstDotIndex + 1, secondDotIndex - 1);

        int prefixSegmentsInt = (firstSegmentInt << 8) + secondSegmentInt;

        int start = ipBlockStart[prefixSegmentsInt], end = ipBlockEnd[prefixSegmentsInt];
        int suffix = calculateIpInteger(ip, 0, secondDotIndex + 1);

        int cur = start == end ? end : binarySearch(start, end, suffix);
        return contentArray[readVInt3(contentIndexes, (cur << 1) + cur)];
    }

    /**
     * 定位ip序号
     *
     * @param low    ip段的起始序号
     * @param high   ip段的结束序号
     * @param suffix
     * @return
     */
    private final int binarySearch(int low, int high, int suffix) {
        int mid = 0;
        while (low <= high) {
            if (low == high) {
                return high;
            }
            mid = (low + high) >> 1;
            switch (compareSuffixBytes(mid, suffix)) {
                case 1:
                    high = mid;
                    break;
                case 0:
                    return mid;
                case -1:
                    low = mid + 1;
            }
        }
        return mid;
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
    private int calculateIpInteger(String ip, int result, int dot) {
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

    /**
     * 对比IP的后两个字节
     *
     * @param index
     * @param suffix
     * @return
     */
    private int compareSuffixBytes(int index, int suffix) {
        int value = endIpBytes[index << 1] << 8 & 0xff00 | endIpBytes[(index << 1) + 1] & 0xff;
        return value > suffix ? 1 : value == suffix ? 0 : -1;
    }

}
