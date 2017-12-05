package com.bob.mvc.service;

import com.bob.mvc.model.BankUser;

/**
 * 银行用户Service
 *
 * @author wb-jjb318191
 * @create 2017-12-05 14:01
 */
public interface BankUserService {

    /**
     * 新增BankUser
     *
     * @param bankUser
     * @return
     */
    public boolean create(BankUser bankUser);

    /**
     * 根据Id查询BankUser
     *
     * @param id
     * @return
     */
    public BankUser retrieveById(Integer id);

}