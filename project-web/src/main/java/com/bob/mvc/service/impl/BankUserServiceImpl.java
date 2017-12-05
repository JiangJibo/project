package com.bob.mvc.service.impl;

import com.bob.mvc.mapper.BankUserMapper;
import com.bob.mvc.model.BankUser;
import com.bob.mvc.service.BankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BankUserService实现类
 *
 * @author wb-jjb318191
 * @create 2017-12-05 14:06
 */
@Service
public class BankUserServiceImpl implements BankUserService {

    @Autowired
    private BankUserMapper bankUserMapper;

    @Override
    public Integer create(BankUser bankUser) {
        bankUserMapper.insertSelective(bankUser);
        return bankUser.getUserId();
    }

    @Override
    public BankUser retrieveById(Integer id) {
        return bankUserMapper.selectByPrimaryKey(id);
    }
}
