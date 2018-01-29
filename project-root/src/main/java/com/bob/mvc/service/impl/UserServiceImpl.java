/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 */
package com.bob.mvc.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.bob.config.mvc.model.User;
import com.bob.config.mvc.userenv.util.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bob.config.mvc.userenv.ann.UserEnv;
import com.bob.mvc.mapper.UserMapper;
import com.bob.mvc.service.UserService;

/**
 * @since 2017年1月23日 上午11:13:15
 * @version $Id$
 * @author JiangJibo
 *
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
