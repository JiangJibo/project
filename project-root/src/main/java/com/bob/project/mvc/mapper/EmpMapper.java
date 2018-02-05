package com.bob.project.mvc.mapper;

import com.bob.project.mvc.entity.model.Emp;

public interface EmpMapper {
    /**
     *  根据主键删除数据库的记录,emp
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,emp
     *
     * @param record
     * @return
     */
    int insert(Emp record);

    /**
     *  动态字段,写入数据库记录,emp
     *
     * @param record
     * @return
     */
    int insertSelective(Emp record);
}