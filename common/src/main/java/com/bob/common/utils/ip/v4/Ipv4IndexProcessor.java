package com.bob.common.utils.ip.v4;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import com.bob.common.utils.ip.IpGeoMetaInfo;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * IP信息索引
 *
 * @author wb-jjb318191
 * @create 2020-03-18 10:59
 */
@SuppressWarnings("Duplicates")
public class Ipv4IndexProcessor {

    private int contentLength;

    private byte[] data;

    /**
     * IP前缀分片个数
     */
    private static final int IP_FIRST_SEGMENT_SIZE = 256 * 256;

    /**
     * ipStartOrder[128.100] = 1000 : 表示以128.100开头第一个IP段的序号是1000
     */
    private int[] ipStartOrder = new int[IP_FIRST_SEGMENT_SIZE];

    /**
     * ipStartOrder[128.100] = 1200 : 表示以128.100开始最后一个IP段的序号是1200
     */
    private int[] ipEndOrder = new int[IP_FIRST_SEGMENT_SIZE];

    /**
     * 每条ip段的结束ip long值
     * endIpInteger[N] = 1256624556, 表示第N个ip段的结束ip的long值是 1256624556
     */
    private List<Integer> endIpInteger;

    /**
     * 内容的位置和长度
     * key: 内容
     * value: offset,length
     */
    private Map<String, String> contentMapping;

    /**
     * 元数据的字节长度
     */
    public static final int META_INFO_BYTE_LENGTH = 1024;

    private static  final String CONTENT_CHAR_SET = "UTF-8";

    /**
     * @param totalIpNum IP总条数
     * @throws Exception
     */
    public Ipv4IndexProcessor(int totalIpNum) throws Exception {
        this.contentLength = META_INFO_BYTE_LENGTH + 4 + 4 + IP_FIRST_SEGMENT_SIZE * 8 + totalIpNum * 12;
        this.data = new byte[contentLength + totalIpNum * 10];

        writeInt(this.data, META_INFO_BYTE_LENGTH, totalIpNum);
        // 4个字节的ip段总数 + 4个字节的内容总数 + 256个ip段 + ip数*12字节的索引信息 + 数据预估
        this.endIpInteger = new ArrayList<>(totalIpNum);
        // 默认内容有65536个
        this.contentMapping = new HashMap<>(1 << 16);
    }

    /**
     * 结束处理
     */
    public void finish() throws Exception {
        // 写入内容条数
        writeInt(data, META_INFO_BYTE_LENGTH + 4, contentMapping.size());
        // 写入每个IP前缀的起始ip序号和结束ip序号
        for (int i = 0; i < IP_FIRST_SEGMENT_SIZE; i++) {
            int k = META_INFO_BYTE_LENGTH + 8 + i * 8;
            writeInt(data, k, ipStartOrder[i]);
            writeInt(data, k + 4, ipEndOrder[i]);
        }
        // 计算MD5
        IpGeoMetaInfo metaInfo = new IpGeoMetaInfo();
        metaInfo.setChecksum(DigestUtils.md5Hex(new ByteArrayInputStream(data, 0, contentLength)));
        String json = JSON.toJSONString(metaInfo);
        byte[] metaData = json.getBytes(CONTENT_CHAR_SET);
        int length = metaData.length;
        // 验证meta长度
        if (4 + length > META_INFO_BYTE_LENGTH) {
            throw new IllegalStateException(
                String.format("meta info length greater than %d , meta info json: %s", META_INFO_BYTE_LENGTH, json));
        }
        // 写入meta信息
        writeInt(data, 0, length);
        System.arraycopy(metaData, 0, data, 4, length);
    }

    private void processContent(String endIp, String address) throws UnsupportedEncodingException {
        String[] ips = endIp.split("\\.");
        int order = endIpInteger.size();

        // ip首段, 比如 1.2.6.5, 首段是 1.2, 位置 1*256+2 = 258
        int firstSegment = Integer.parseInt(ips[0]) * 256 + Integer.parseInt(ips[1]);
        // 初始化起始序号
        if (ipStartOrder[firstSegment] == 0) {
            ipStartOrder[firstSegment] = order;
        }
        ipEndOrder[firstSegment] = order;
        // 计算ip后几段的int值
        int endIpInt = calculateIpInteger(endIp, 2);
        endIpInteger.add(endIpInt);

        int contentLength, length;
        // 如果这个内容写过了
        if (contentMapping.containsKey(address)) {
            String[] split = contentMapping.get(address).split(",");
            // 写内容索引的offset
            contentLength = Integer.parseInt(split[0]);
            length = Integer.parseInt(split[1]);
        } else { // 新的内容
            byte[] bytes = address.getBytes(CONTENT_CHAR_SET);
            length = bytes.length;
            contentMapping.put(address, this.contentLength + "," + length);
            // 写内容
            System.arraycopy(bytes, 0, data, this.contentLength, length);
            contentLength = this.contentLength;
            this.contentLength += length;
        }

        // 待写入位置
        int nextPos = META_INFO_BYTE_LENGTH + 8 + IP_FIRST_SEGMENT_SIZE * 8 + order * 12;
        // 写结束ip
        writeInt(data, nextPos, endIpInt);
        // 写内容位置
        writeInt(data, nextPos + 4, contentLength);
        // 写内容的长度
        writeInt(data, nextPos + 8, length);
    }

    public void writeInt(byte[] data, int offset, int i) {
        writeVLong4(data, offset, i);
    }

    /**
     * 写入4个字节的long
     *
     * @param data
     * @param offset
     * @param i
     */
    public void writeVLong4(byte[] data, int offset, long i) {
        data[offset++] = (byte)(i >> 24);
        data[offset++] = (byte)(i >> 16);
        data[offset++] = (byte)(i >> 8);
        data[offset] = (byte)i;
    }

    /**
     * 计算ip后几段的数值
     *
     * @param ip
     * @param firstSegment
     * @return
     */
    private int calculateIpInteger(String ip, int firstSegment) {
        int result = 0;
        String[] splits = ip.split("\\.");
        for (int i = firstSegment; i < splits.length; i++) {
            result <<= 8;
            result |= Long.parseLong(splits[i]) & 0xff;
        }
        return result;
    }

    public byte[] getData() {
        return data;
    }

    public int getContentOffset() {
        return contentLength;
    }
}
