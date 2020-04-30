package com.bob.common.utils.ip.v4;

/**
 * @author 牧茗
 * @email yym98625@alibaba-inc.com
 * @date 2020/04/08 23:38
 */
public class IPv4BitCalc {

    public static int readInt(byte[] data, int p) {
        return (int) readVLong4(data, p);
    }

    public static long readVLong4(byte[] data, int p) {
        return ((data[p++] << 24) & 0xFF000000L) | readVInt3(data, p);
    }

    public static int readVInt3(byte[] data, int p) {
        return (int) (((data[p++] << 16) & 0xFF0000L) | ((data[p++] << 8) & 0xFF00L) | (data[p++] & 0xFFL));
    }
}
