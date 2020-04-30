package com.bob.common.utils.ip.v6;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import com.bob.common.utils.ip.IpGeoMetaInfo;
import org.apache.commons.io.FileUtils;

import static com.bob.common.utils.ip.DigestUtils.rawToJSON;
import static com.bob.common.utils.ip.v4.IPv4BitCalc.readInt;

/**
 * @author wb-jjb318191
 * @create 2020-04-30 9:44
 */
public class Ipv6SearchProcessor {

    /**
     * v6 a段索引区占用字节数
     */
    private static final int IP_FIRST_SEGMENT_SIZE = 256 * 256;

    /**
     * ipBlockStartIndex[1][128.100] = 1000 : 表示以128.100开头第一个IP段的序号是1000
     */
    private int[][] ipBlockStartIndex;

    /**
     * ipBlockStartIndex[128.100] = 1200 : 表示以128.100开始最后一个IP段的序号是1200
     */
    private int[][] ipBlockEndIndex;

    /**
     * 不同字节长度的ip信息,ip差值,内容位置
     */
    private byte[][] diffLengthIpInfos;

    private String[] contentArray;

    private ThreadLocal<byte[][]> cacheBytePool = new ThreadLocal<>();

    /**
     * 内容位置字节长度
     */
    private int contentIndexByteLength;

    /**
     * 元数据的字节长度
     */
    private static final int META_INFO_BYTE_LENGTH = 1024;

    /**
     * 内容字符编码
     */
    private static final String CONTENT_CHAR_SET = "utf-8";

    public final boolean load(File file, IpGeoMetaInfo metaInfo) throws Exception {
        byte[] data = FileUtils.readFileToByteArray(file);
        // 唯一性的内容条数
        this.contentArray = new String[readInt(data, META_INFO_BYTE_LENGTH)];
        // 内容位置占字节数, 内容条数一般3字节足够了
        this.contentIndexByteLength = contentArray.length < 1 << 16 ? 2 : 3;

        // 有多少种不同长度就生成多少个字节数组
        this.diffLengthIpInfos = new byte[16][];
        this.ipBlockStartIndex = new int[16][];
        this.ipBlockEndIndex = new int[16][];

        int offset = META_INFO_BYTE_LENGTH + 4 + 16 * 4;

        for (int i = 0; i < 16; i++) {
            // 此字节长度下的ip数
            int num = readInt(data, META_INFO_BYTE_LENGTH + 4 + i * 4);
            if (num > 0) {
                // 6字节的ip ： 4字节后缀 + 4字节的ip差值 + 2/3字节的内容序号
                // 16字节的ip ： 14字节后缀 + 14字节的ip差值 + 2/3字节的内容序号
                int length = (2 * (i + 1 - 2) + contentIndexByteLength) * num;
                this.diffLengthIpInfos[i] = new byte[length];
                ipBlockStartIndex[i] = new int[IP_FIRST_SEGMENT_SIZE];
                ipBlockEndIndex[i] = new int[IP_FIRST_SEGMENT_SIZE];
            }
        }
        // 加载每个ip段的起始序号和结束序号
        for (int i = 0; i < 16; i++) {
            // 此长度的ip不存在
            if (diffLengthIpInfos[i] == null) {
                offset += 8 * IP_FIRST_SEGMENT_SIZE;
                continue;
            }
            for (int j = 0; j < IP_FIRST_SEGMENT_SIZE; j++) {
                // 0:0:0:0:0:12:2e:3000  :  存储 6字节长度的ip 的 12 开头的ip, 他们的序号在 100~1000之间, 是6字节内部的序号
                ipBlockStartIndex[i][j] = readInt(data, offset);
                ipBlockEndIndex[i][j] = readInt(data, offset + 4);
                offset += 8;
            }
        }
        // 加载每个ip段的数据信息,ip后段, ip差值, 内容位置
        for (int i = 0; i < diffLengthIpInfos.length; i++) {
            byte[] bytes = diffLengthIpInfos[i];
            if (bytes != null) {
                System.arraycopy(data, offset, bytes, 0, bytes.length);
                offset += bytes.length;
            }
        }

        // 加载内容
        for (int i = 0; i < contentArray.length; i++) {
            int length = readInt(data, offset);
            offset += 4;
            // 将所有字符串都取出来, 每个字符串都缓存好
            String rawContent = new String(Arrays.copyOfRange(data, offset, offset + length), CONTENT_CHAR_SET);
            String content = rawToJSON(rawContent, Arrays.asList(metaInfo.getStoredProperties()),
                new HashSet<>(Arrays.asList(metaInfo.getLoadProperties())));
            contentArray[i] = content;
            offset += length;
        }
        return true;
    }

    public final String search(String ip) {
        // 将ip转换成字节数组
        byte[] iPv6Address = toByteArray(ip);
        // 当前字节长度在所有长度中的序号, 从0开始
        int segmentIndex = iPv6Address.length - 1;
        // 当前长度下的ip没有
        if (ipBlockStartIndex[segmentIndex] == null) {
            return null;
        }
        int prefixSegmentsInt = ((iPv6Address[0] & 0xff) << 8) + (iPv6Address[1] & 0xff);
        int start = ipBlockStartIndex[segmentIndex][prefixSegmentsInt];
        int end = ipBlockEndIndex[segmentIndex][prefixSegmentsInt];

        if (start < 0) {
            return null;
        }
        int cur = binarySearch(start, end, iPv6Address);
        // ip地址未分配
        if (cur == -1) {
            return null;
        }
        return contentArray[cur];
    }

    /**
     * 定位ip序号
     *
     * @param low  ip段的起始序号
     * @param high ip段的结束序号
     * @param ip   IP字节数组, 16字节
     * @return int
     */
    private int binarySearch(int low, int high, byte[] ip) {
        // 存储当前长度的ip信息的数组
        byte[] data = diffLengthIpInfos[ip.length - 1];

        // 如果是一字节或者二字节的ip
        if (ip.length <= 2) {
            return readVInt(data, high * contentIndexByteLength, contentIndexByteLength);
        }
        // 每份ip信息的字节长度
        int perLength = ((ip.length - 2) << 1) + contentIndexByteLength;

        int result = -2, order = high;
        while (low <= high) {
            int mid = (low + high) >> 1;
            result = compareByteArray(data, mid * perLength, ip, 2);
            // 如果结束的ip就是当前ip
            if (result == 0) {
                order = mid;
                break;
            } else if (result > 0) {
                order = mid;
                if (mid == 0) {
                    break;
                }
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        int offset = order * perLength;
        if (result != 0 && !compareByStartIp(ip, 2, data, offset + ip.length - 2)) {
            return -1;
        }
        return readVInt(data, offset + ((ip.length - 2) << 1), contentIndexByteLength);
    }

    /**
     * 读取3个字节凑成Int
     *
     * @param data
     * @param p
     * @return
     */
    private int readVInt(byte[] data, int p, int length) {
        if (length == 2) {
            return (int)(((data[p++] << 8) & 0xFF00L) | (data[p++] & 0xFFL));
        } else {
            return (int)(((data[p++] << 16) & 0xFF0000L) | ((data[p++] << 8) & 0xFF00L) | (data[p++] & 0xFFL));
        }
    }

    public byte[] toByteArray(String address) {

        if (address == null) {
            throw new IllegalArgumentException("Attempted to parse null address");
        }
        // 验证长度
        int first = 0, last = address.length();
        if (address.length() < 2) {
            throw new IllegalArgumentException(
                String.format("Invalid length - the string %s is too short to be an IPv6 address", address));
        }
        int length = last - first;
        if (length > 39) { // 32个数字 + 7个":"
            throw new IllegalArgumentException(
                String.format("Invalid length - the string %s is too long to be an IPv6 address. Length: %d",
                    address, address.length()));
        }
        int partIndex = 0, partHexDigitCount = 0, afterDoubleSemicolonIndex = last + 2;
        // 获得缓存的字节数组
        byte[][] pool = cacheBytePool.get();
        if (pool == null) {
            initCacheBytePool();
            pool = cacheBytePool.get();
        }
        byte[] data = null;
        byte v;
        int k = 0, s = 0, x = 1, left = 0;
        for (int i = first; i < last; i++) {
            char c = address.charAt(i);
            if (isHexDigit(c)) {
                if (c == '0' && data == null) {
                    continue;
                }
                // 定位当前段有几个数字, 且当前数字的序号：1 2 3 4 中的一个
                if (partHexDigitCount == 0) {
                    int y = i + 1;
                    until:
                    {
                        if (y >= last || address.charAt(y++) == ':') {
                            break until;
                        } else {
                            x++;
                        }
                        if (y >= last || address.charAt(y++) == ':') {
                            break until;
                        } else {
                            x++;
                        }
                        if (y >= last || address.charAt(y++) == ':') {
                            break until;
                        } else {
                            x++;
                        }
                    }
                    // ipv6分8段, 每一段都会写入两个字节
                    left = left + 2;
                    // 当前数字的序号
                    partHexDigitCount = 4 - x;
                }
                // 初始化字节数组
                if (data == null) {
                    int dataLength = ((7 - partIndex) << 1) + (x > 2 ? 2 : 1);
                    data = pool[dataLength - 1];
                }
                // 每段数字不能超过4个
                if (++partHexDigitCount > 4) {
                    throw new IllegalArgumentException(
                        String
                            .format("Ipv6 address %s parts must contain no more than 16 bits (4 hex digits)", address));
                }
                // 当前数字的字节值
                v = c >= 97 ? (byte)((c - 87) & 0xff) : (byte)((c - 48) & 0xff);
                // 0:0:12:c::12:226 将'c'的前一字节填充0
                if (x == 1 && partHexDigitCount == 4 && k > 0) {
                    data[k] = 0;
                    s++;
                    k++;
                }
                // 0:0:12:2c::12:226 将'2c'的前一字节填充0
                if (x == 2 && partHexDigitCount == 3 && k > 0) {
                    data[k] = 0;
                    s++;
                    k++;
                }
                // 根据数字位置填充字节的前一半
                if (partHexDigitCount == 1 || partHexDigitCount == 3) {
                    data[k] = (byte)(v << 4);
                }
                // 填充字节的后一半
                if (partHexDigitCount == 2 || partHexDigitCount == 4) {
                    // 如果此段只有一个数字,或者有3个数字，当前是第一个数字
                    if (x == 1 || (partHexDigitCount == 2 && x == 3)) {
                        data[k] = 0;
                    }
                    data[k] = (byte)(data[k] | v);
                    s++;
                    k++;
                }
            } else {
                if (c == ':') {
                    if (data != null) {
                        // 下一次写的字节位置
                        if (k != 1) {
                            k += 2 - s;
                        }
                    }
                    s = 0;
                    x = 1;
                    partIndex++;
                    partHexDigitCount = 0;
                    // 如果存在连续的两个":", 即 "::"
                    if (i < last - 1 && address.charAt(i + 1) == ':') {
                        // 在两个: 之后的下一个数字的位置
                        afterDoubleSemicolonIndex = i + 2;
                        break;
                    }
                    continue;
                }
                throw new IllegalArgumentException(
                    String.format("Ipv6 address %s illegal character: %c at index %d", address, c, i));
            }
        }

        // 从末尾倒叙向前遍历 直至 ::
        int lastFilledPartIndex = partIndex - 1, l = data.length - 1, right = l;
        partIndex = 7;
        for (int i = last - 1; i >= afterDoubleSemicolonIndex; i--) {
            char c = address.charAt(i);
            if (isHexDigit(c)) {
                if (partIndex <= lastFilledPartIndex) {
                    throw new IllegalArgumentException(
                        String.format("Ipv6 address %s too many parts. Expected 8 parts", address));
                }
                if (++partHexDigitCount > 4) {
                    throw new IllegalArgumentException(
                        String
                            .format("Ipv6 address %s parts must contain no more than 16 bits (4 hex digits)", address));
                }
                // 当前数字的字节值
                v = c >= 97 ? (byte)((c - 87) & 0xff) : (byte)((c - 48) & 0xff);
                // 根据数字位置填充字节的后一半
                if (partHexDigitCount == 1 || partHexDigitCount == 3) {
                    right--;
                    data[l] = v;
                }
                // 填充字节的前一半
                if (partHexDigitCount == 2 || partHexDigitCount == 4) {
                    data[l] = (byte)(data[l] | v << 4);
                    s++;
                    l--;
                }
            } else {
                if (c == ':') {
                    l -= 2 - s;
                    s = 0;
                    partIndex--;
                    partHexDigitCount = 0;
                    continue;
                }
                throw new IllegalArgumentException(
                    String.format("Ipv6 address %s illegal character: %c at index %d", address, c, i));
            }
        }
        // 填充 "::" 代表的空字节, 置为0, 因为字节数组是缓存重用的, 需要复位
        if (left != right) {
            for (int i = left; i <= right; i++) {
                data[i] = 0;
            }
        }
        return data;
    }

    private static boolean isHexDigit(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f');
    }

    /**
     * 初始化ThreadLocal的二维字节数组
     */
    private void initCacheBytePool() {
        //缓存待用字节数组
        byte[][] cacheBytes = new byte[16][];
        for (int i = 0; i < cacheBytes.length; i++) {
            cacheBytes[i] = new byte[i + 1];
        }
        cacheBytePool.set(cacheBytes);
    }

    /**
     * 比较两个字节数字的大小,逐位比较
     *
     * @param array1 数组1
     * @param index1 开始的比较位
     * @param array2 数组2
     * @param index2 开始的比较位
     * @return int
     */
    private int compareByteArray(byte[] array1, int index1, byte[] array2, int index2) {
        int k = 0, result = 0;
        for (int i = index1; i < index1 + array2.length; i++) {
            if (array1[i] != array2[index2 + k]) {
                return (array1[i] & 0xff) - (array2[index2 + k] & 0xff);
            }
            // end ip 和当前检索ip相同
            if (index2 + ++k == array2.length) {
                return 0;
            }
        }
        return result;
    }

    /**
     * 和起始ip比较
     *
     * @param ip         检索ip
     * @param index
     * @param data       startIp后缀
     * @param startIndex
     * @return
     */
    private boolean compareByStartIp(byte[] ip, int index, byte[] data, int startIndex) {
        int k = 0;
        for (int i = index; i < ip.length; i++) {
            int x1 = ip[i] & 0xff, x2 = data[startIndex + k] & 0xff;
            if (x1 < x2) {
                return false;
            } else if (x1 > x2) {
                return true;
            }
            k++;
        }
        return true;
    }

}
