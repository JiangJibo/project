package com.bob.common.utils.request.post.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 种子类型
 *
 * @author wb-jjb318191
 * @create 2019-07-17 10:46
 */
public enum SeedType {
    // oo
    OO(0, "OO"),

    // wx
    WX(1, "微信"),

    // tg
    TEG(2, "telegram");

    private Integer code;
    private String label;

    private static final Map<Integer, SeedType> VALUES = new HashMap<>();

    static {
        for (SeedType seedType : SeedType.values()) {
            VALUES.put(seedType.code, seedType);
        }
    }

    SeedType(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public static SeedType valueOf(Integer code) {
        return VALUES.get(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

}