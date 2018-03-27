/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 */
package com.bob.web.mvc.mapper;

import com.bob.common.entity.BaseMapper;
import com.bob.web.config.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年1月23日 上午11:13:57
 */
public interface UserMapper extends BaseMapper<Integer, User> {

    /**
     * 通过用户名登录
     *
     * @param userName
     * @param password
     * @return
     */
    User loginByName(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过手机号码登录
     *
     * @param telephone
     * @param password
     * @return
     */
    User loginByTele(@Param("telephone") String telephone, @Param("password") String password);

}
