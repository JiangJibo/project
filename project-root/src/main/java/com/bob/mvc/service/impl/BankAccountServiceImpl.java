package com.bob.mvc.service.impl;

import com.bob.mvc.mapper.BankAccountMapper;
import com.bob.mvc.model.BankAccount;
import com.bob.mvc.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wb-jjb318191
 * @create 2017-12-29 9:32
 */
@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Override
    public BankAccount getById(Integer id) {
        return bankAccountMapper.selectByPrimaryKey(id);
    }
}
