package com.bob.common.utils.ip;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 牧茗
 * @email yym98625@alibaba-inc.com
 * @date 2020/04/08 23:26
 */
public class DigestUtils {

    /**
     * geo地理位置信息某个字段为空时候的返回值
     */
    public static final String NOTFOUND_GEO_ITEM_VALUE = "";

    /**
     * 分隔符，不可打印字符
     */
    public static final String GEO_RAW_SEP = "\0";

    private static final char[] DIGITS_LOWER =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5Hex(final byte[] bytes) {
        byte[] data;
        try {
            data = MessageDigest.getInstance("MD5").digest(bytes);
        } catch (NoSuchAlgorithmException exp) {
            return null;
        }

        final int l = data.length;
        final char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

    /**
     * 解码内容, 生成json
     *
     * @param rawContent
     * @param storedProperties 所有属性
     * @param loadProperties   加载属性
     * @return
     */
    public static String rawToJSON(String rawContent, List<String> storedProperties, Set<String> loadProperties) {
        String[] splits = rawContent.split(GEO_RAW_SEP, storedProperties.size());
        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < storedProperties.size(); i++) {
            String value = NOTFOUND_GEO_ITEM_VALUE;
            if (i < splits.length) {
                value = splits[i].trim().length() > 0 ? splits[i].trim() : NOTFOUND_GEO_ITEM_VALUE;
            }
            if (loadProperties.contains(storedProperties.get(i))) {
                jsonObject.put(storedProperties.get(i), value);
            }
        }
        return jsonObject.toJSONString();
    }
}
