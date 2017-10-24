package com.bob.mvc.mapper;

import com.bob.mvc.model.BankUser;

public interface BankUserMapper {
    /**
     *  根据主键删除数据库的记录,bank_user
     *
     * @param userId
     * @return
     */
    int deleteByPrimaryKey(Integer userId);

    /**
     *  新写入数据库记录,bank_user
     *
     * @param record
     * @return
     */
    int insert(BankUser record);

    /**
     *  动态字段,写入数据库记录,bank_user
     *
     * @param record
     * @return
     */
    int insertSelective(BankUser record);

    /**
     *  根据指定主键获取一条数据库记录,bank_user
     *
     * @param userId
     * @return
     */
    BankUser selectByPrimaryKey(Integer userId);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,bank_user
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(BankUser record);

    /**
     *  根据主键来更新符合条件的数据库记录,bank_user
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(BankUser record);
}