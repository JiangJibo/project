package com.bob.project.mvc.service.impl;

import java.util.List;
import java.util.Map;

import com.bob.project.mvc.mapper.BankUserMapper;
import com.bob.project.mvc.entity.model.BankUser;
import com.bob.project.mvc.service.BankAccountService;
import com.bob.project.mvc.service.BankUserService;
import com.bob.project.utils.validate.DataValidate;
import com.bob.project.utils.validate.ValidateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.bob.project.utils.validate.Group.A;

/**
 * BankUserService实现类
 *
 * @author wb-jjb318191
 * @create 2017-12-05 14:06
 */
@Service
public class BankUserServiceImpl implements BankUserService {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankUserMapper bankUserMapper;

    @Override
    public boolean create(BankUser bankUser) {
        ValidateProcessor.doValidating(bankUser, A);
        return true;
        //return bankUserMapper.insertSelective(bankUser) > 0;
    }

    @Override
    public BankUser retrieveById(Integer id) {
        bankAccountService.getById(id);
        return bankUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public Map<Integer, BankUser> retrieveByIds(List<Integer> ids) {
        Assert.notEmpty(ids, "通过id集合查询BankUser时，id集合不能为空");
        return bankUserMapper.selectByIds(ids);
    }

    @Override
    public List<BankUser> retrieveByAgeForPages(Integer age, int currentPage, int size) {
        Assert.isTrue(currentPage > 0, "当前页码必须大于0");
        return bankUserMapper.selectByPages(age, size * (currentPage - 1) + 1, size);
    }
}
