package com.bob.project.mvc.service;

import java.util.List;
import java.util.Map;

import com.bob.project.mvc.entity.model.BankUser;

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

    /**
     * 根据Id集合查询BankUser
     *
     * @param ids
     * @return
     */
    public Map<Integer, BankUser> retrieveByIds(List<Integer> ids);

    /**
     * 分页查询指定年龄的BankUser
     *
     * @param age
     * @param currentPage
     * @param size
     * @return
     */
    public List<BankUser> retrieveByAgeForPages(Integer age, int currentPage, int size);

}