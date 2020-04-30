package com.bob.common.utils.ip;

import java.util.Calendar;

import lombok.Data;

/**
 * 文件meta信息
 *
 * @author wb-jjb318191
 * @create 2020-03-27 14:00
 */
@Data
public class IpGeoMetaInfo {

    /**
     * 文件版本： semvar格式, 字符串长度最长是8位
     */
    private String version;

    /**
     * 文件md5，包括meta头: 固定32位的md5
     */
    private String checksum;

    /**
     * 文件打包时间：unix时间戳（秒），固定字符串长度10
     */
    private Long gmtCreate = System.currentTimeMillis() / 1000;

    /**
     * ipv4还是ipv6， ipv4为4， ipv6为6
     */
    private Integer ipversion = 4;

    /**
     * 过期时间, 默认包生成时间+20年
     */
    private Long expireAt;

    /**
     * dat文件里面包含的geo字段, 按顺序
     */
    private String[] storedProperties = {"country", "province", "city", "county", "isp"};

    /**
     * dat文件里面包含的geo字段, 按顺序
     */
    private String[] loadProperties = {"country", "province", "city", "county", "isp"};

    public IpGeoMetaInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 20);
        this.expireAt = calendar.getTimeInMillis() / 1000;
    }

}
