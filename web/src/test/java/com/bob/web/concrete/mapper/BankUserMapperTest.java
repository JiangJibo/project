package com.bob.web.concrete.mapper;

import java.util.Arrays;
import java.util.Date;

import com.bob.web.config.BaseControllerTest;
import com.bob.web.mvc.entity.form.BankUserForm;
import com.bob.web.mvc.mapper.BankUserMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Administrator
 * @create 2018-04-01 20:07
 */
public class BankUserMapperTest extends BaseControllerTest {

    @Autowired
    private BankUserMapper bankUserMapper;

    @Override
    protected void init() {
    }

    @Test
    public void testListString2VarcharTypeHandler() {
        BankUserForm form = new BankUserForm();
        form.setAdresses(Arrays.asList("上海,杭州,北京"));
        form.setAge(30);
        form.setUsername("王小二");
        form.setBirthday(new Date());
        form.setIdcard("330881201804011254");
        form.setAdress("");
        form.setPhoneNumber("18758745852");
        form.setSex(1);
        bankUserMapper.insertWithAdresses(form);
        System.out.println(form.getUserId());
    }


}
