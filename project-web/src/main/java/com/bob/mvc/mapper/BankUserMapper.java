package com.bob.mvc.mapper;

import java.util.List;
import java.util.Map;

import com.bob.config.root.mapper.BaseMapper;
import com.bob.mvc.model.BankUser;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * 银行用户Mapper接口
 *
 * @author wb-jjb318191
 * @create 2017-12-06 9:23
 */
public interface BankUserMapper extends BaseMapper<Integer, BankUser> {

    /**
     * 根据Ids查询BankUser,返回的结果为Map集合,Key指定为userId
     *
     * @param ids
     * @return
     */
    @MapKey("userId")
    public Map<Integer, BankUser> selectByIds(List<Integer> ids);

    /**
     * 分页查询指定年龄的BankUser
     *
     * @param age
     * @param limit
     * @param offset
     * @return
     */
    public List<BankUser> selectByPages(@Param("age") Integer age, @Param("offset") Integer offset, @Param("limit") Integer limit);

}