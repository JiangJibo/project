package com.bob.common.utils.ip.v6;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import com.bob.common.utils.ip.IpGeoMetaInfo;
import com.google.common.collect.Lists;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author wb-jjb318191
 * @create 2020-04-30 9:40
 */
public class Ipv6IndexProcessor {

    private byte[] data;

    /**
     * 每个长度字节的ip数量
     */
    private int[] blockIpNum;

    /**
     * 相同ip前缀的ip起始序号, 在相同长度内部的序号
     */
    private List<int[]> ipBlockStartIndex;

    /**
     * 相同ip前缀的ip结束序号, 在相同长度内部的序号
     */
    private List<int[]> ipBlockEndIndex;

    /**
     * 不同字节长度的ip信息,ip差值,内容位置
     */
    private List<List<Byte>> diffLengthIpInfos;

    /**
     * 内容
     */
    private List<String> contents;

    /**
     * 内容的位置和长度
     * key: 内容
     * value: {@link #contents}位置
     */
    private Map<String, Integer> contentMapping;

    private int contentLength;

    /**
     * 内容字符编码
     */
    private static final String CONTENT_CHAR_SET = "utf-8";

    /**
     * 元数据的字节长度
     */
    private static final int META_INFO_BYTE_LENGTH = 1024;

    /**
     * IP前缀分片个数
     */
    private static final int IP_FIRST_SEGMENT_SIZE = 256 * 256;

    public boolean init() {
        this.blockIpNum = new int[16];

        this.ipBlockStartIndex = new ArrayList<>(16);
        this.ipBlockEndIndex = new ArrayList<>(16);
        this.diffLengthIpInfos = new ArrayList(16);

        for (int i = 0; i < 16; i++) {
            int[] ints1 = new int[IP_FIRST_SEGMENT_SIZE];
            Arrays.fill(ints1, -1);
            ipBlockStartIndex.add(ints1);
            int[] ints2 = new int[IP_FIRST_SEGMENT_SIZE];
            Arrays.fill(ints2, -1);
            ipBlockEndIndex.add(ints2);
            diffLengthIpInfos.add(new ArrayList<>());
        }

        this.contents = new ArrayList<>(1 << 14);
        this.contentMapping = new HashMap<>(1 << 14);
        return true;
    }

    public void index(String s, String e, String content) {
        byte[] startIp = new BigInteger(s).toByteArray();
        byte[] endIp = new BigInteger(e).toByteArray();
        int length = endIp.length;
        // 补充startIp长度
        if (startIp.length != length) {
            int l = startIp.length;
            byte[] bytes = new byte[length];
            System.arraycopy(startIp, 0, bytes, length - l, l);
            startIp = bytes;
        }
        // 有效字节长度
        int effectiveBytesLength = endIp[0] != 0 ? length : calculateEffectiveLength(endIp);
        int index = effectiveBytesLength - 1;
        // 有效首字节位置
        // ip首段, 比如 1.2.6.5, 首段是 1.2, 位置 1*256+2 = 258
        int startIndex = length - effectiveBytesLength;

        int segment;
        if (startIndex == length - 1) {  // 兼容一个字节的ip
            segment = endIp[length - 1] & 0xff;
        } else {
            segment = (endIp[startIndex] & 0xff) * 256 + (endIp[startIndex + 1] & 0xff);
        }

        // 获取当前ip的插入顺序, 比如是7个长度字节的地100个ip
        int order = blockIpNum[index];
        // 初始化起始序号, 当前字节长度内的序号
        int[] startBlock = ipBlockStartIndex.get(index);
        if (startBlock[segment] == -1) {
            startBlock[segment] = order;
        }

        // 初始化结束序号, 当前字节长度内的序号
        int[] endBlock = ipBlockEndIndex.get(index);
        endBlock[segment] = order;

        // 如果ip在256*256之前
        if (effectiveBytesLength <= 2) {
            // 起始ip的段序号
            int startSegment = effectiveBytesLength == 1 ? startIp[length - 1] & 0xff
                : (startIp[length - 2] & 0xff) * 256 + (startIp[length - 1] & 0xff);

            for (int i = startSegment; i < segment; i++) {
                startBlock[i] = order;
                endBlock[i] = order;
            }
        }

        // 插入序号自增
        blockIpNum[index] = ++order;

        int contentOffset;
        // 如果这个内容写过了
        if (contentMapping.containsKey(content)) {
            // 写内容索引的offset
            contentOffset = contentMapping.get(content);
        } else {  // 新的内容
            this.contents.add(content);
            contentOffset = contents.size() - 1;
            contentMapping.put(content, contentOffset);
        }
        List<Byte> segmentInfo = diffLengthIpInfos.get(index);
        if (startIndex + 2 < length) {
            // 写ip后缀
            listAddArray(segmentInfo, Arrays.copyOfRange(endIp, startIndex + 2, length));
            // 写ip前缀
            listAddArray(segmentInfo, Arrays.copyOfRange(startIp, startIndex + 2, length));
        }
        // 写内容位置, 默认3个字节
        listAddArray(segmentInfo, to3ByteArray(contentOffset));
    }

    public void finish() throws Exception {
        // 如果内容数小于 256*256,则用2个字节存储
        if (contents.size() < 1 << 16) {
            reduceContentIndexByteLength();
        }

        //4个字节的内容总数 + 16种字节长度 * 4 (每种长度下的ip数)
        // +  16*256*256*8字节 + ...
        int contentOffset = META_INFO_BYTE_LENGTH + 4 + 16 * 4 + 16 * IP_FIRST_SEGMENT_SIZE * 8
            + calculateTotalListSize(diffLengthIpInfos);

        this.data = new byte[contentOffset + sumIpNum() * 50];

        // 4字节内容数
        writeInt(this.data, META_INFO_BYTE_LENGTH, contents.size());
        // 写入字节长度的ip条数
        for (int i = 0; i < blockIpNum.length; i++) {
            writeInt(this.data, META_INFO_BYTE_LENGTH + 4 + i * 4, blockIpNum[i]);
        }
        // 把多个字节长度的ip的前缀序号都写入数组
        int nextOffset = META_INFO_BYTE_LENGTH + 4 + 16 * 4;
        for (int x = 0; x < 16; x++) {
            // 写入每个IP前缀的起始ip序号和结束ip序号
            for (int i = 0; i < IP_FIRST_SEGMENT_SIZE; i++) {
                writeInt(data, nextOffset, ipBlockStartIndex.get(x)[i]);
                writeInt(data, nextOffset + 4, ipBlockEndIndex.get(x)[i]);
                nextOffset += 8;
            }
        }
        // 写ip后缀,ip差值,内容位置
        for (int i = 0; i < diffLengthIpInfos.size(); i++) {
            List<Byte> list = diffLengthIpInfos.get(i);
            arrayAddList(data, list, nextOffset);
            // 比如6个字节有7000条ip, 写入ip条数
            nextOffset += list.size();
        }
        // 写内容
        for (String content : contents) {
            byte[] bytes = content.getBytes(CONTENT_CHAR_SET);
            writeInt(data, nextOffset, bytes.length);
            nextOffset += 4;
            System.arraycopy(bytes, 0, data, nextOffset, bytes.length);
            nextOffset += bytes.length;
        }

        this.contentLength = nextOffset;

        // 计算MD5
        IpGeoMetaInfo meta = new IpGeoMetaInfo();
        meta.setIpversion(6);
        meta.setChecksum(DigestUtils.md5Hex(new ByteArrayInputStream(data, 0, nextOffset)));
        String json = JSON.toJSONString(meta);
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

    private void writeInt(byte[] data, int offset, int i) {
        writeVLong4(data, offset, i);
    }

    /**
     * 计算字节的有效长度
     *
     * @param bytes
     * @return
     */
    private int calculateEffectiveLength(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                return bytes.length - i;
            }
        }
        return bytes.length;
    }

    /**
     * 缩减内容位置所占字节数
     */
    private void reduceContentIndexByteLength() {
        for (int i = 0; i < 16; i++) {
            List<Byte> bytes = diffLengthIpInfos.get(i);
            if (bytes.isEmpty()) {
                continue;
            }
            int perLength = i < 2 ? 3 : 2 * (i + 1 - 2) + 3;
            List<Byte> newBytes = new ArrayList<>(bytes.size());
            List<List<Byte>> partitions = Lists.partition(bytes, perLength);
            // 缩减内容字节数, 由3字节减为2字节
            for (List<Byte> bts : partitions) {
                for (int k = 0; k < bts.size(); k++) {
                    if (k != perLength - 2 - 1) {
                        newBytes.add(bts.get(k));
                    }
                }
            }
            diffLengthIpInfos.remove(i);
            diffLengthIpInfos.add(i, newBytes);
        }
    }

    /**
     * 写入4个字节的long
     *
     * @param data   字节数据
     * @param offset 偏移量
     * @param i      值
     */
    private void writeVLong4(byte[] data, int offset, long i) {
        data[offset++] = (byte)(i >> 24);
        data[offset++] = (byte)(i >> 16);
        data[offset++] = (byte)(i >> 8);
        data[offset] = (byte)i;
    }

    private void listAddArray(List<Byte> list, byte[] array) {
        for (byte t : array) {
            list.add(t);
        }
    }

    private void arrayAddList(byte[] array, List<Byte> list, int offset) {
        for (byte b : list) {
            array[offset++] = b;
        }
    }

    /**
     * 用3个字节存储int
     *
     * @param n
     * @return
     */
    private byte[] to3ByteArray(int n) {
        byte[] b = new byte[3];
        b[2] = (byte)(n & 0xff);
        b[1] = (byte)(n >> 8 & 0xff);
        b[0] = (byte)(n >> 16 & 0xff);
        return b;
    }

    private int calculateTotalListSize(List<List<Byte>> dataList) {
        int length = 0;
        for (List<Byte> list : dataList) {
            length += list.size();
        }
        return length;
    }

    private int sumIpNum() {
        int sum = 0;
        for (int i : blockIpNum) {
            sum += i;
        }
        return sum;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getContentOffset() {
        return this.contentLength;
    }

}
