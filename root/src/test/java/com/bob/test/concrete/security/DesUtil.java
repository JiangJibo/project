package com.bob.test.concrete.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES加密 解密算法
 *
 * DES算法的入口参数有三个：Key、Data、Mode。其中Key为8个字节共64位，是DES算法的工作密钥；Data也为8个字节64位，
 * 是要被加密或被解密的数据；Mode为DES的工作方式，有两种：加密或解密。
 * DES算法是这样工作的：如Mode为加密，则用Key 去把数据Data进行加密， 生成Data的密码形式（64位）作为DES的输出结果；
 * 如 Mode为解密，则用Key去把密码形式的数据Data解密，还原为Data的明码形式（64位）作为DES的输出结果。
 * 在通信网络的两端，双方约定一致的Key，在通信的源点用Key对核心数据进行DES加密，然后以密码形式在公共通信网（如电话网）中传输到通信网络的终点，
 * 数据到达目的地后，用同样的 Key对密码数据进行解密，便再现了明码形式的核心数据。这样，便保证了核心数据（如PIN、MAC等）在公共通信网中传输的安全性和可靠性。
 * 通过定期在通信网络的源端和目的端同时改用新的Key，便能更进一步提高数据的保密性，这正是现在金融交易网络的流行做法。
 *
 * 下面是具体代码：（切记切记 字符串转字节或字节转字符串时 一定要加上编码，否则可能出现乱码）
 *
 * @author wb-jjb318191
 */
public class DesUtil {

    private static final String DES = "DES";
    private static final String ENCODE = "GBK";
    private static final String DEFAULT_KEY = "adminmap";
    private static final String DATA = "雄安_2017-12-07";

    /**
     * 测试加密解密
     */
    @Test
    public void testEncryptDecrypt() throws Exception {
        String key = DEFAULT_KEY;
        System.out.println("加密前的信息为:" + DATA);
        String encryptInfo = DesUtil.encrypt(DATA, key);
        System.out.println("加密后的信息为:" + encryptInfo);
        System.out.println("解密后的信息为:" + DesUtil.decrypt(encryptInfo, key));
    }

    /**
     * 使用 默认key 加密
     *
     * @param data
     * @return String
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        return encrypt(data, DEFAULT_KEY);
    }

    /**
     * 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        if (data == null) {
            return null;
        }
        byte[] bt = encrypt(data.getBytes(ENCODE), key.getBytes(ENCODE));
        return new BASE64Encoder().encode(bt);
    }

    /**
     * 使用 默认key 解密
     *
     * @param data
     * @return String
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {
        return decrypt(data, DEFAULT_KEY);
    }

    /**
     * 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        if (data == null) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }

    /**
     * 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretkey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secretkey, sr);
        return cipher.doFinal(data);
    }

    /**
     * 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretkey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secretkey, sr);
        return cipher.doFinal(data);
    }
}