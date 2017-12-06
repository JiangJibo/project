/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 */
package com.bob.mvc.mapper;

import com.bob.config.mvc.model.User;
import com.bob.config.root.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @since 2017年1月23日 上午11:13:57
 * @version $Id$
 * @author JiangJibo
 *
 */
public interface UserMapper extends BaseMapper<Integer, User> {

    /**
     * 通过用户名登录
     *
     * @param userName
     * @param password
     * @return
     */
    public User loginByName(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过手机号码登录
     *
     * @param telephone
     * @param password
     * @return
     */
    public User loginByTele(@Param("telephone") String telephone, @Param("password") String password);

}
