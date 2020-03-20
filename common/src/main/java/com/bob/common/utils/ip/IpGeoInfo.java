package com.bob.common.utils.ip;

/**
 * @author chenxiangyu
 */
public class IpGeoInfo {

    private static final int FEILDS_NUM = 5;

    private String startIp;

    private String endIp;

    private String country;
    private String province;
    private String city;
    private String isp;
    private String county;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public IpGeoInfo() {

    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    public String getEndIp() {
        return endIp;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }

    public IpGeoInfo(String info) {

        String[] rawFields = info.split("\\|", -1);
        if (rawFields.length >= FEILDS_NUM) {
            this.setCountry(rawFields[0]);
            this.setProvince(rawFields[1]);
            this.setCity(rawFields[2]);
            this.setCounty(rawFields[3]);
            this.setIsp(rawFields[4]);
        }
    }
}
