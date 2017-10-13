package com.bob.mvc.mapper;

import com.bob.mvc.model.BankAccount;
import com.bob.mvc.model.BankAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BankAccountMapper {
    /**
     *  根据指定的条件获取数据库记录数,bank_account
     *
     * @param example
     * @return
     */
    long countByExample(BankAccountExample example);

    /**
     *  根据指定的条件删除数据库符合条件的记录,bank_account
     *
     * @param example
     * @return
     */
    int deleteByExample(BankAccountExample example);

    /**
     *  根据主键删除数据库的记录,bank_account
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

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
    int insertSelective(BankAccount record);

    /**
     *  根据指定的条件查询符合条件的数据库记录,bank_account
     *
     * @param example
     * @return
     */
    List<BankAccount> selectByExample(BankAccountExample example);

    /**
     *  根据指定主键获取一条数据库记录,bank_account
     *
     * @param id
     * @return
     */
    BankAccount selectByPrimaryKey(Integer id);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录,bank_account
     *
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") BankAccount record, @Param("example") BankAccountExample example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录,bank_account
     *
     * @param record
     * @param example
     * @return
     */
    int updateByExample(@Param("record") BankAccount record, @Param("example") BankAccountExample example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,bank_account
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(BankAccount record);

    /**
     *  根据主键来更新符合条件的数据库记录,bank_account
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(BankAccount record);
}