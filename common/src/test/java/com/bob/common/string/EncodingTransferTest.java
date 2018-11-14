package com.bob.common.string;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

/**
 * String�ֽ�ת������
 * GBK������һ������2���ֽڣ���UTF-8������һ������3���ֽڣ������ǵ���getBytes("UTF-8")����ʱ��
 * ��ͨ�������������ֽڣ�ʹ�ô�GBK��2���ֽڱ��UTF-8��Ӧ��3���ֽڡ�
 * �����UTF-8
 *
 * @author wb-jjb318191
 * @create 2018-11-13 13:58
 */
public class EncodingTransferTest {

    /**
     * DEBUG�鿴 utf8Bytes �� gbkBytes �����һλ�ֽڲ�ͬ
     *
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testEncode() throws UnsupportedEncodingException {
        String utf8 = "hello,��ð�";
        // [104, 101, 108, 108, 111, 44, -28, -67, -96, -27, -91, -67, -27, -107, -118]
        byte[] utf8Bytes = utf8.getBytes();
        String gbk = new String(utf8.getBytes("UTF-8"), "GBK");
        // [104, 101, 108, 108, 111, 44, -28, -67, -96, -27, -91, -67, -27, -107, 63]
        byte[] gbkBytes = gbk.getBytes("GBK");
        System.out.println(new String(gbkBytes));
    }

    @Test
    public void testTransfer() throws Exception {
        String ss = "iteye�ʴ�";
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
        String ss = new String("hello,��~~~��~~~��".getBytes(),"UTF-8");
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
