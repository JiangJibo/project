package com.bob.project.test.concrete.enumtest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 等级枚举
 *
 * @author wb-jjb318191
 * @create 2017-11-20 10:01
 */
public enum Level {

    DIAMOND("钻石", 10) {
        @Override
        public float getDiscount() {
            return 0.88f;
        }

        @Override
        public String toString() {
            return "label:"+name()+",折扣:"+getCode();
        }
    },

    PLATINUM("铂金", 8) {
        @Override
        public float getDiscount() {
            return 0.9f;
        }
    },

    GOLE("黄金", 6) {
        @Override
        public float getDiscount() {
            return 0.92f;
        }
    },

    SILVER("白银", 4) {
        @Override
        public float getDiscount() {
            return 0.94f;
        }
    },

    COPPER("青铜", 2) {
        @Override
        public float getDiscount() {
            return 0.96f;
        }
    },

    COMMON("普通", 0) {
        @Override
        public float getDiscount() {
            return 0.98f;
        }
    };

    private String label;
    private Integer code;

    private static final Map<Integer, Level> VALUES = new LinkedHashMap<Integer, Level>();

    Level(String label, Integer code) {
        this.label = label;
        this.code = code;
    }

    static {
        for (Level level : Level.values()) {
            VALUES.put(level.code, level);
        }
    }

    public static Level valueOf(Integer code) {
        return VALUES.get(code);
    }

    /**
     * 获取折扣
     *
     * @return
     */
    public abstract float getDiscount();

    public String getLabel() {
        return label;
    }

    public Integer getCode() {
        return code;
    }
}