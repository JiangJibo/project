package com.bob.common.utils.ip;

import java.util.Calendar;

/**
 * 文件meta信息
 *
 * @author wb-jjb318191
 * @create 2020-03-27 14:00
 */
public class Ipv4MetaInfo {

    /**
     * 文件版本： semvar格式, 字符串长度最长是8位
     */
    private String version ;

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
    private String[] geoFields = {"country", "province", "city", "county", "isp"};

    public Ipv4MetaInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 20);
        this.expireAt = calendar.getTimeInMillis() / 1000;
    }

    public String getVersion() {
        return version;
    }

    public String getChecksum() {
        return checksum;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public Integer getIpversion() {
        return ipversion;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public String[] getGeoFields() {
        return geoFields;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setIpversion(Integer ipversion) {
        this.ipversion = ipversion;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }

    public void setGeoFields(String[] geoFields) {
        this.geoFields = geoFields;
    }
}
