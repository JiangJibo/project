package com.bob.web.mvc.entity.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * 区域类型枚举
 *
 * @author wb-jjb318191
 * @create 2019-05-10 14:40
 */
public enum ZoneEnum {
    /**
     * 一区
     */
    ZONE1("ZONE1", "1区", 1),

    /**
     * 二区
     */
    ZONE2("ZONE2", "2区", 1),

    /**
     * 三区
     */
    ZONE3("ZONE3", "3区", 1),

    /**
     * 四区
     */
    ZONE4("ZONE4", "4区", 1),

    /**
     * 五区
     */
    ZONE5("ZONE5", "5区", 1),

    /**
     * 六区
     */
    ZONE6("ZONE6", "6区", 1),

    /**
     * 七区
     */
    ZONE7("ZONE7", "7区", 1),

    /**
     * 八区
     */
    ZONE8("ZONE8", "8区", 1),

    /**
     * 九区
     */
    ZONE9("ZONE9", "9区", 1),

    /**
     * 偏远邮编1区
     */
    REMOTE1("REMOTE1", "偏远邮编1区", 2),

    /**
     * 偏远邮编2区
     */
    REMOTE2("REMOTE2", "偏远邮编2区", 2);

    private final String zoneCode;
    private final String desc;

    /**
     * 1:标准,2:偏远
     */
    private final int type;

    private ZoneEnum(String zoneCode, String desc, int type) {
        this.zoneCode = zoneCode;
        this.desc = desc;
        this.type = type;
    }

    public static boolean isRemote(String zoneCode) {
        ZoneEnum zoneEnum = getByZoneCode(zoneCode);
        if (zoneEnum != null && zoneEnum.getType() == 2) {
            return true;
        } else {
            return false;
        }
    }

    public static ZoneEnum getByZoneCode(String zoneCode) {
        ZoneEnum[] enums = ZoneEnum.values();
        return Arrays.stream(enums).filter(Objects::nonNull).filter(e -> e.getZoneCode().equals(zoneCode)).findAny().orElse(null);
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public String getDesc() {
        return desc;
    }

    public int getType() {
        return type;
    }
}

