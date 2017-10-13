package com.bob.mvc.mapper;

import com.bob.mvc.model.BankUser;
import com.bob.mvc.model.BankUserExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BankUserMapper {
    /**
     *  根据指定的条件获取数据库记录数,bank_user
     *
     * @param example
     * @return
     */
    long countByExample(BankUserExample example);

    /**
     *  根据指定的条件删除数据库符合条件的记录,bank_user
     *
     * @param example
     * @return
     */
    int deleteByExample(BankUserExample example);

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
     *  根据指定的条件查询符合条件的数据库记录,bank_user
     *
     * @param example
     * @return
     */
    List<BankUser> selectByExample(BankUserExample example);

    /**
     *  根据指定主键获取一条数据库记录,bank_user
     *
     * @param userId
     * @return
     */
    BankUser selectByPrimaryKey(Integer userId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录,bank_user
     *
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") BankUser record, @Param("example") BankUserExample example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录,bank_user
     *
     * @param record
     * @param example
     * @return
     */
    int updateByExample(@Param("record") BankUser record, @Param("example") BankUserExample example);

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