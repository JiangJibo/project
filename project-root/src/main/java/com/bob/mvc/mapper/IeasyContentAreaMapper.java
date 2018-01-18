package com.bob.mvc.mapper;

import com.bob.mvc.model.IeasyContentArea;

public interface IeasyContentAreaMapper {
    /**
     *  根据主键删除数据库的记录,ieasy_content_area
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,ieasy_content_area
     *
     * @param record
     * @return
     */
    int insert(IeasyContentArea record);

    /**
     *  动态字段,写入数据库记录,ieasy_content_area
     *
     * @param record
     * @return
     */
    int insertSelective(IeasyContentArea record);

    /**
     *  根据指定主键获取一条数据库记录,ieasy_content_area
     *
     * @param id
     * @return
     */
    IeasyContentArea selectByPrimaryKey(Long id);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,ieasy_content_area
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(IeasyContentArea record);

    /**
     *  根据主键来更新符合条件的数据库记录,ieasy_content_area
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(IeasyContentArea record);
}