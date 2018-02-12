/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 */
package com.bob.web.mvc.service.impl;

import com.bob.web.mvc.mapper.UserMapper;
import com.bob.web.mvc.service.UserService;
import com.bob.web.config.model.User;
import com.bob.web.config.userenv.ann.UserEnv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年1月23日 上午11:13:15
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(User user) {
        String userName = user.getUserName();
        String password = user.getPassword();
        User result = null;
        if (Character.isDigit(userName.charAt(0))) {
            result = userMapper.loginByTele(userName, password);
        } else {
            result = userMapper.loginByName(userName, password);
        }
        return result;
    }

    @Override
    public boolean modifyUser(@UserEnv User user) {
        return false;
    }

}
