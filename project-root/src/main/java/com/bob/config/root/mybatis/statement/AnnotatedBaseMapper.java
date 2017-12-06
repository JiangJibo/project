package com.bob.config.root.mybatis.statement;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Mybatis基础Mapper
 *
 * @author wb-jjb318191
 * @create 2017-09-08 14:37
 */
public interface AnnotatedBaseMapper<T> {

    /**
     * 插入语句
     *
     * @param bean
     * @return
     */
    @Options(useGeneratedKeys = true)
    @InsertProvider(type = SqlProvider.class, method = "insert")
    public int insert(T bean);

    /**
     * 删除语句
     *
     * @param bean
     * @return
     */
    @DeleteProvider(type = SqlProvider.class, method = "delete")
    public int delete(T bean);

    /**
     * 更新语句
     *
     * @param bean
     * @return
     */
    @UpdateProvider(type = SqlProvider.class, method = "update")
    public int update(T bean);

    /**
     * 查找语句
     *
     * @param bean
     * @return
     */
    @SelectProvider(type = SqlProvider.class, method = "findFirst")
    public T findFirst(T bean);

}