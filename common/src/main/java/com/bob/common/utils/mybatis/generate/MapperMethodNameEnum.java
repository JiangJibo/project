package com.bob.common.utils.mybatis.generate;

/**
 * Mapper接口方法名称枚举
 * 可将通过逆向工程生成的方法名称映射为新的方法名称
 * 比如将 selectByPrimaryKey >>  selectById
 *
 * @author wb-jjb318191
 * @create 2018-08-01 9:30
 */
public enum MapperMethodNameEnum {

    INSERT("insert", null),
    INSERT_SELECTIVE("insertSelective", null),
    DELETE_BY_PRIMARY_KEY("deleteByPrimaryKey", null),
    UPDATE_BY_PRIMARY_KEY("updateByPrimaryKey", null),
    UPDATE_BY_PRIMARY_KEY_SELECTIVE("updateByPrimaryKeySelective", null),
    SELECT_BY_PRIMARY_KEY("selectByPrimaryKey", null);

    /**
     * 原始方法名称
     */
    private String rawName;
    /**
     * 新的方法名称
     */
    private String newName;

    MapperMethodNameEnum(String rawValue, String newName) {
        this.rawName = rawValue;
        this.newName = newName;
    }

    public String getRawName() {
        return rawName;
    }

    public String getNewName() {
        return newName;
    }
}