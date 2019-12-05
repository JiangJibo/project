package com.bob.root.concrete.file;

import java.io.UnsupportedEncodingException;

import com.google.common.base.Preconditions;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * TokenUtils
 *
 * @author yangcanfeng
 * @version 2018/12/26 15:25:01
 */
public class TokenUtils {

    /**
     * createSignToken
     *
     * @param applyCode
     * @return
     */
    public static String createSignToken(String applyCode) {
        Preconditions.checkArgument(StringUtils.isNotBlank(applyCode), "applyCode must not be blank.");
        String signToken = StringUtils.EMPTY;
        try {
            byte[] b2 = DigestUtils.md5Hex(applyCode).getBytes("utf-8");
            signToken = DigestUtils.md5Hex(b2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signToken;
    }

    public static void main(String[] args) {
        System.out.println(createSignToken("membercenter"));
    }

}
