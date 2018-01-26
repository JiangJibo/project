package com.bob.mvc.service;

import com.bob.mvc.entity.model.BankAccount;

/**
 * 银行账户Service
 *
 * @author wb-jjb318191
 * @create 2017-12-29 9:31
 */
public interface BankAccountService {

    /**
     * @param id
     * @return
     */
    public BankAccount getById(Integer id);

}