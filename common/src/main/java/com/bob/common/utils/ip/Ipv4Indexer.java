package com.bob.common.utils.ip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * IP信息索引
 *
 * @author wb-jjb318191
 * @create 2020-03-18 10:59
 */
public class Ipv4Indexer {

    /**
     * {@link #data} 里内容下一个可写的下标
     */
    private int contentPosition;

    private byte[] data;

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
    private List<Long> endIpLong;

    /**
     * 内容的位置和长度
     * key: 内容
     * value: offset,length
     */
    private Map<String, String> addressMapping;

    /**
     * 内容字符编码
     */
    private static final String CONTENT_CHAR_SET = "UTF-8";

    private static final int IP_FIRST_SEGMENT_SIZE = 256;

    /**
     * @param totalIpNum IP总条数
     */
    public Ipv4Indexer(int totalIpNum) {
        // 下一个内容可写入位置
        this.contentPosition = 4 + 4 + IP_FIRST_SEGMENT_SIZE * 8 + totalIpNum * 9;
        this.data = new byte[4 + 4 + IP_FIRST_SEGMENT_SIZE * 8 + totalIpNum * 9 + totalIpNum * 60];
        // 写ip总数
        writeInt(this.data, 0, totalIpNum);
        // 4个字节的ip段总数 + 4个字节的内容总数 + 256个ip段 + ip数*9字节的索引信息 + 数据预估
        this.endIpLong = new ArrayList<>(totalIpNum);
        // 默认内容有65536个
        this.addressMapping = new HashMap<>(1 << 16);
    }

    /**
     * @param infos
     */
    public void process(List<IpGeoInfo> infos) {

    }

    /**
     * 结束处理
     */
    public void finishProcessing() {
        // 写入内容条数
        writeInt(data, 4, addressMapping.size());
        // 写入每个IP前缀的起始ip序号和结束ip序号
        for (int i = 0; i < IP_FIRST_SEGMENT_SIZE; i++) {
            int k = (i + 1) * 8;
            writeInt(data, k, ipStartOrder[i]);
            writeInt(data, k + 4, ipEndOrder[i]);
        }
    }

    /**
     * 刷新数据到文件
     *
     * @param path
     * @throws IOException
     */
    public void flushData(String path) throws IOException {
        File file = new File(path);
        FileUtils.writeByteArrayToFile(file, data, 0, contentPosition);
    }

    public void indexIpInfo(String startIp, String endIp, String address) throws Exception {
        String[] ips = endIp.split("\\.");
        // 当前ip段的序号
        int order = endIpLong.size();
        // ip首段
        int firstSegment = Integer.valueOf(ips[0]);
        // 初始化起始序号
        if (ipStartOrder[firstSegment] == 0) {
            ipStartOrder[firstSegment] = order;
        }
        ipEndOrder[firstSegment] = order;

        long endLong = ipToLong(endIp);
        endIpLong.add(endLong);

        int contentOffset, length;
        // 如果这个内容写过了
        if (addressMapping.containsKey(address)) {
            String split[] = addressMapping.get(address).split(",");
            // 写内容索引的offset
            contentOffset = Integer.valueOf(split[0]);
            length = Integer.valueOf(split[1]);
        }
        // 新的内容
        else {
            byte[] bytes = address.getBytes(CONTENT_CHAR_SET);
            length = bytes.length;
            addressMapping.put(address, contentPosition + "," + length);
            // 写内容
            System.arraycopy(bytes, 0, data, contentPosition, bytes.length);
            contentOffset = contentPosition;
            contentPosition += length;
        }
        // 待写入位置
        int offset = 8 + IP_FIRST_SEGMENT_SIZE * 8 + order * 9;
        // 写结束ip
        writeVLong4(data, offset, endLong);
        // 写内容位置
        writeInt(data, offset + 4, contentOffset);
        // 写内容的长度
        writeByte(data, offset + 8, length);
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

    @SuppressWarnings("Duplicates")
    private long ipToLong(String ip) {
        long result = 0;
        String[] d = ip.split("\\.");
        for (String b : d) {
            result <<= 8;
            result |= Long.parseLong(b) & 0xff;
        }
        return result;
    }

    /**
     * ip: 1.1.2.5; 1.3.5.8
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        File txt = new File("C:\\Users\\JiangJibo\\Desktop\\全球旗舰版-202002-636871\\全球旗舰版-202002-636871.txt");
        List<String> lines = FileUtils.readLines(txt, "utf-8");
        Ipv4Indexer indexer = new Ipv4Indexer(lines.size());
        List<String> ips = new ArrayList<>();
        for (String line : lines) {
            String[] splits = line.split("\\|");
            StringBuilder sb = new StringBuilder();
            for (int i = 4; i < splits.length; i++) {
                sb.append(splits[i]).append("|");
            }
            String text = sb.toString();
            indexer.indexIpInfo(splits[0], splits[1], text.substring(0, text.length() - 1));
            ips.add(splits[0]);
        }

        indexer.finishProcessing();
        File dat = new File("C:\\Users\\JiangJibo\\Desktop\\ipv4-utf8-index.dat");
        if (dat.exists()) {
            dat.delete();
        }
        indexer.flushData(dat.getPath());
        FileUtils.writeLines(new File("C:\\Users\\JiangJibo\\Desktop\\ips.txt"), ips);
    }

}
