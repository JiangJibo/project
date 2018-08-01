package com.bob.web.mvc.mapper;

import com.bob.web.mvc.entity.model.BankAccount;

public interface BankAccountMapper {
    /**
     *  根据主键删除数据库的记录,bank_account
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     *  新写入数据库记录,bank_account
     *
     * @param record
     * @return
     */
    int insert(BankAccount record);

    /**
     *  动态字段,写入数据库记录,bank_account
     *
     * @param record
     * @return
     */
    int insertWithoutNull(BankAccount record);

    /**
     *  根据指定主键获取一条数据库记录,bank_account
     *
     * @param id
     * @return
     */
    BankAccount selectById(Integer id);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,bank_account
     *
     * @param record
     * @return
     */
    int updateByIdWithoutNull(BankAccount record);

    /**
     *  根据主键来更新符合条件的数据库记录,bank_account
     *
     * @param record
     * @return
     */
    int updateById(BankAccount record);
}
