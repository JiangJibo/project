package com.bob.project.mvc.service.impl;

import com.bob.project.mvc.mapper.BankAccountMapper;
import com.bob.project.mvc.entity.model.BankAccount;
import com.bob.project.mvc.service.BankAccountService;
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
