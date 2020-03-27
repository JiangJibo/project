package com.bob.common.utils.ip;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

/**
 * IP信息索引
 *
 * @author wb-jjb318191
 * @create 2020-03-18 10:59
 */
public class Ipv4IndexProcessor {

    /**
     * {@link #data} 里内容下一个可写的下标
     */
    private int contentOffset;

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
    private Map<String, String> addressMapping;

    /**
     * 元数据
     */
    private Ipv4MetaInfo metaInfo;

    /**
     * 内容字符编码
     */
    private static final String CONTENT_CHAR_SET = "utf-8";

    /**
     * 元数据的字节长度
     */
    private static final int META_INFO_BYTE_LENGTH = 1024;

    /**
     * @param totalIpNum IP总条数
     * @param metaInfo   元数据
     */
    public Ipv4IndexProcessor(int totalIpNum, Ipv4MetaInfo metaInfo) throws Exception {
        String metaJson = JSON.toJSONString(metaInfo);
        // 验证meta长度
        if (4 + metaJson.getBytes(CONTENT_CHAR_SET).length > META_INFO_BYTE_LENGTH) {
            throw new IllegalStateException(
                String.format("meta info length greater than %d , meta info json: %s", META_INFO_BYTE_LENGTH, metaJson));
        }
        // 下一个内容可写入位置
        this.contentOffset = META_INFO_BYTE_LENGTH + 4 + 4 + IP_FIRST_SEGMENT_SIZE * 8 + totalIpNum * 9;
        this.data = new byte[META_INFO_BYTE_LENGTH + 4 + 4 + IP_FIRST_SEGMENT_SIZE * 8 + totalIpNum * 9
            + totalIpNum * 15];
        // 写ip总数
        writeInt(this.data, META_INFO_BYTE_LENGTH, totalIpNum);
        // 4个字节的ip段总数 + 4个字节的内容总数 + 256个ip段 + ip数*9字节的索引信息 + 数据预估
        this.endIpInteger = new ArrayList<>(totalIpNum);
        // 默认内容有65536个
        this.addressMapping = new HashMap<>(1 << 16);
        this.metaInfo = metaInfo;
    }

    /**
     * 结束处理
     */
    public void finishProcessing() throws Exception {
        // 写入内容条数
        writeInt(data, META_INFO_BYTE_LENGTH + 4, addressMapping.size());
        // 写入每个IP前缀的起始ip序号和结束ip序号
        for (int i = 0; i < IP_FIRST_SEGMENT_SIZE; i++) {
            int k = META_INFO_BYTE_LENGTH + 8 + i * 8;
            writeInt(data, k, ipStartOrder[i]);
            writeInt(data, k + 4, ipEndOrder[i]);
        }
        // 处理 meta 信息
        Arrays.fill(data, 0, 1024, (byte)0);
        // 计算MD5
        this.metaInfo.setChecksum(DigestUtils.md5Hex(new ByteArrayInputStream(data, 0, contentOffset)));
        String json = JSON.toJSONString(this.metaInfo);
        byte[] metaData = JSON.toJSONString(this.metaInfo).getBytes(CONTENT_CHAR_SET);
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

    /**
     * 刷新数据到文件
     *
     * @param path
     * @throws IOException
     */
    public void flushData(String path) throws IOException {
        FileUtils.writeByteArrayToFile(new File(path), data, 0, contentOffset);
    }

    public void indexIpInfo(String endIp, String address) throws Exception {
        String[] ips = endIp.split("\\.");
        // 当前ip段的序号
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

        int contentOffset, length;
        // 如果这个内容写过了
        if (addressMapping.containsKey(address)) {
            String split[] = addressMapping.get(address).split(",");
            // 写内容索引的offset
            contentOffset = Integer.parseInt(split[0]);
            length = Integer.parseInt(split[1]);
        }
        // 新的内容
        else {
            byte[] bytes = address.getBytes(CONTENT_CHAR_SET);
            length = bytes.length;
            addressMapping.put(address, this.contentOffset + "," + length);
            // 写内容
            System.arraycopy(bytes, 0, data, this.contentOffset, bytes.length);
            contentOffset = this.contentOffset;
            this.contentOffset += length;
        }
        // 待写入位置
        int nextPos = META_INFO_BYTE_LENGTH + 8 + IP_FIRST_SEGMENT_SIZE * 8 + order * 9;
        // 写结束ip
        writeInt(data, nextPos, endIpInt);
        // 写内容位置
        writeInt(data, nextPos + 4, contentOffset);
        // 写内容的长度
        writeByte(data, nextPos + 8, length);
    }

    public void writeInt(byte[] data, int offset, int i) {
        writeVLong4(data, offset, i);
    }

    public void writeByte(byte[] data, int offset, int i) {
        data[offset] = (byte)(i & 0xFF);
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
}
