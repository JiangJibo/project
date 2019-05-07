package com.bob.common.string;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

/**
 * String字节转换问题
 * GBK编码是一个中文2个字节，而UTF-8编码是一个中文3个字节，当我们调用getBytes("UTF-8")方法时，
 * 会通过计算来增加字节，使得从GBK的2个字节变成UTF-8对应的3个字节。
 * 如果从UTF-8
 *
 * @author wb-jjb318191
 * @create 2018-11-13 13:58
 */
public class EncodingTransferTest {

    /**
     * DEBUG查看 utf8Bytes 和 gbkBytes 的最后一位字节不同
     *
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testEncode() throws UnsupportedEncodingException {
        String utf8 = "hello,你好啊";
        // [104, 101, 108, 108, 111, 44, -28, -67, -96, -27, -91, -67, -27, -107, -118]
        byte[] utf8Bytes = utf8.getBytes();
        String gbk = new String(utf8.getBytes("UTF-8"), "GBK");
        // [104, 101, 108, 108, 111, 44, -28, -67, -96, -27, -91, -67, -27, -107, 63]
        byte[] gbkBytes = gbk.getBytes("GBK");
        System.out.println(new String(gbkBytes));
    }

    @Test
    public void testTransfer() throws Exception {
        String ss = "iteye问答啊";
        String gbk2iso = new String(ss.getBytes("GBK"), "ISO-8859-1");
        System.out.println(gbk2iso);
        String iso2gbk = new String(gbk2iso.getBytes("ISO-8859-1"), "GBK");
        System.out.println(iso2gbk);
        String utf8 = new String(gbk2iso.getBytes("ISO-8859-1"), "UTF-8");
        System.out.println(utf8);
        String gbk = new String(gbk2iso.getBytes("UTF-8"), "UTF-8");
    }

    @Test
    public void testBase64() throws UnsupportedEncodingException {
        String ss = new String("hello,测~~~试~~~啊".getBytes(),"UTF-8");
        String encode = base64Encode(ss, "GBK");
        ss = base64Decode(encode, "GBK");
        System.out.println(ss);
    }


    private String base64Encode(String content, String charset) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(content.getBytes(charset)), charset);
    }

    private String base64Decode(String content, String charset) throws UnsupportedEncodingException {
        return new String(Base64.decodeBase64(content.getBytes(charset)), charset);
    }
}
