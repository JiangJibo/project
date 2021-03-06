package com.bob.web.mvc.mapper;

import java.util.List;
import java.util.Map;

import com.bob.common.entity.base.BaseMapper;
import com.bob.web.mvc.entity.form.BankUserForm;
import com.bob.web.mvc.entity.model.BankUser;
import com.bob.web.mvc.entity.vo.BankUserVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

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
    Map<Integer, BankUser> selectByIds(List<Integer> ids);

    /**
     * 分页查询指定年龄的BankUser
     *
     * @param age
     * @param limit
     * @param offset
     * @return
     */
    List<BankUser> selectByPages(@Param("age") Integer age, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * @param userId
     * @param age
     * @return
     */
    BankUser selectByIdAndAge(@Param("userId") Integer userId, @Param("age") Integer age);

    /**
     * 测试List<String>转换为String的TypeHandler
     *
     * @param form
     * @return
     */
    int insertWithAdresses(BankUserForm form);

    /**
     * 测试List<String>转换为String的TypeHandler
     *
     * @param userId
     * @return
     */
    BankUserVO selectByUserId(int userId);

    /**
     * 测试 #{} 和 ${} 混合后Mybatis的处理结果
     *
     * @param form
     * @return
     */
    BankUserVO selectByForm(BankUserForm form);

    /**
     * @param userName
     * @return
     */
    BankUserVO selectByName(String userName);

}