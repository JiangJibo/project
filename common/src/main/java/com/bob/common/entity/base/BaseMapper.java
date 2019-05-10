package com.bob.common.entity.base;

/**
 * Mapper基类接口
 *
 * @author wb-jjb318191
 * @create 2017-12-06 9:23
 */
public interface BaseMapper<K, T> {

    /**
     * 根据主键删除数据库的记录
     *
     * @param id
     * @return
     */
    int deleteById(K id);

    /**
     * 新写入数据库记录
     *
     * @param record
     * @return
     */
    int insert(T record);

    /**
     * 动态字段,写入数据库记录
     *
     * @param record
     * @return
     */
    int insertWithoutNull(T record);

    /**
     * 根据指定主键获取一条数据库记录
     *
     * @param id
     * @return
     */
    T selectById(K id);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     * @return
     */
    int updateByIdWithoutNull(T record);

    /**
     * 根据主键来更新符合条件的数据库记录
     *
     * @param record
     * @return
     */
    int updateById(T record);


}