package com.bob.common.utils.mybatis.generate.constant;

/**
 * Mapper接口方法名称y映射
 * 可将通过逆向工程生成的方法名称映射为新的方法名称
 * 比如将 selectByPrimaryKey >>  selectById
 *
 * @author wb-jjb318191
 * @create 2018-08-01 9:30
 */
public enum MapperMethodMappings {

    INSERT("insert", null),
    INSERT_SELECTIVE("insertSelective", "insertWithoutNull"),
    DELETE_BY_PRIMARY_KEY("deleteByPrimaryKey", "deleteById"),
    UPDATE_BY_PRIMARY_KEY("updateByPrimaryKey", "updateById"),
    UPDATE_BY_PRIMARY_KEY_SELECTIVE("updateByPrimaryKeySelective", "updateByIdWithoutNull"),
    SELECT_BY_PRIMARY_KEY("selectByPrimaryKey", "selectById");

    /**
     * 原始方法名称
     */
    private String source;
    /**
     * 新的方法名称
     */
    private String target;

    MapperMethodMappings(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}