package com.bob.common.utils.ip;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author wb-jjb318191
 * @create 2020-03-18 18:21
 */
public class Ipv4SearchProcessor {

    /**
     * ipv4 a段索引区占用字节数
     */
    private static final int IP_FIRST_SEGMENT_SIZE = 256 * 256;

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
     * ipStartOrder[128.100] = 1256335 : 表示以128.100开头的后两个ip段的int值是1256335
     */
    private int[] endIpInteger;

    /**
     * addressIndex[N] = 1000, 表示第N个ip段的数据是 {@link #addressArray} 的地N个位置的字符串
     */
    private int[] addressIndex;

    private String[] addressArray;

    /**
     * 元数据的字节长度
     */
    private static final int META_INFO_BYTE_LENGTH = 1024;

    /**
     * 内容字符编码
     */
    private static final String CONTENT_CHAR_SET = "utf-8";

    private Ipv4SearchProcessor(String filePath, String[] properties) throws Exception {
        this.init(filePath, properties);
    }

    private boolean init(String filePath, String[] propertyArray) throws Exception {
        byte[] data;
        try {
            data = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new IllegalArgumentException(
                String.format("IPV4 data file path not applicable, path:{%s}", filePath));
        }

        // 处理 meta 信息
        int metaLength = readInt(data, 0);
        String metaInfo = new String(Arrays.copyOfRange(data, 4, 4 + metaLength), CONTENT_CHAR_SET);
        JSONObject jsonObject = JSON.parseObject(metaInfo);
        // 如果到期了
        if (jsonObject.getLong("expireAt") < System.currentTimeMillis() / 1000) {
            throw new IllegalAccessException("ip search service is expire");
        }
        // 所有属性
        List<String> properties = jsonObject.getJSONArray("geoFields").toJavaList(String.class);
        // 移除meta信息, 方便计算md5
        Arrays.fill(data, 0, 4 + metaLength, (byte)0);

        // 校验 MD5
        String md5 = DigestUtils.md5Hex(data);
        if (!md5.equals(jsonObject.getString("checksum"))) {
            throw new IllegalStateException("data file has been modified, md5 not match");
        }

        for (int i = 0; i < IP_FIRST_SEGMENT_SIZE; i++) {
            int k = META_INFO_BYTE_LENGTH + 8 + i * 8;
            ipStartOrder[i] = readInt(data, k);
            ipEndOrder[i] = readInt(data, k + 4);
        }
        // 前4字节存储ip条数
        int recordSize = readInt(data, META_INFO_BYTE_LENGTH);
        endIpInteger = new int[recordSize];
        addressIndex = new int[recordSize];
        addressArray = new String[readInt(data, META_INFO_BYTE_LENGTH + 4)];

        // 加载属性
        Set<String> loadProperties;
        if (propertyArray == null || propertyArray.length == 0) {
            loadProperties = new HashSet<>(properties);
        } else {
            loadProperties = new HashSet<>(Arrays.asList(propertyArray));
        }

        int index = 0;
        // 原始内容与处理过的内容间的映射
        Map<String, String> contentMappings = new HashMap<>();
        Map<String, Integer> contentIndexMappings = new HashMap<>();

        for (int i = 0; i < recordSize; i++) {
            // 8 + 256*8 +
            int pos = META_INFO_BYTE_LENGTH + 8 + IP_FIRST_SEGMENT_SIZE * 8 + (i * 9);
            // 前4字节存储结束的ip值
            this.endIpInteger[i] = readInt(data, pos);
            int offset = readInt(data, pos + 4);
            int length = data[pos + 8] & 0xff;
            // 将所有字符串都取出来, 每个字符串都缓存好
            String rawContent = new String(Arrays.copyOfRange(data, offset, offset + length), CONTENT_CHAR_SET);
            // 对原始内容做处理, 不重复处理
            String content;
            if (contentMappings.containsKey(rawContent)) {
                content = contentMappings.get(rawContent);
            } else {
                content = decodeContent(rawContent, properties, loadProperties);
                contentMappings.put(rawContent, content);
            }
            // 缓存字符串, 如果是一个新的字符串
            if (!contentIndexMappings.containsKey(content)) {
                addressArray[index] = content;
                contentIndexMappings.put(content, index);
                addressIndex[i] = index++;
            } else {
                addressIndex[i] = contentIndexMappings.get(content);
            }
        }
        return true;
    }

    public static Ipv4SearchProcessor newInstance(String path, String[] properties) throws Exception {
        return new Ipv4SearchProcessor(path, properties);
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
        int firstSegment = calculateIpSegmentInt(ip, 0, firstDotIndex - 1);

        int secondDotIndex = ip.indexOf(".", firstDotIndex + 1);
        int secondSegment = calculateIpSegmentInt(ip, firstDotIndex + 1, secondDotIndex - 1);
        firstSegment = (firstSegment << 8) + secondSegment;

        int start = ipStartOrder[firstSegment], end = ipEndOrder[firstSegment];
        int cur = start == end ? end : binarySearch(start, end, calculateIpInteger(ip, 0, secondDotIndex + 1));
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
    private int binarySearch(int low, int high, int k) {
        int order = 0;
        while (low <= high) {
            int mid = (low + high) >> 1;
            if (endIpInteger[mid] >= k) {
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

}
