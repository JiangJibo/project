package com.bob.test.controller;

import java.util.Calendar;

import com.bob.mvc.controller.BankController;
import com.bob.mvc.model.BankUser;
import com.bob.test.config.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 银行控制器测试类
 *
 * @author wb-jjb318191
 * @create 2017-12-05 14:19
 */
public class BankControllerTest extends BaseControllerTest {

    @Autowired
    private BankController bankController;

    @Override
    protected void init() {
        super.loginBefore = false;
        super.mappedController = bankController;
    }

    @Test
    public void testCreateUser(){
        BankUser bankUser = new BankUser();
        bankUser.setEmail("q450210669@163.com");
        bankUser.setSex(1);
        bankUser.setIdcard("330881198807262314");
        bankUser.setPhoneNumber("18758107760");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1988,7,26);
        bankUser.setBirthday(calendar.getTime());
        bankUser.setUsername("JiangJibo");
        bankUser.setAdress("杭州余杭");
        bankUser.setAge(29);
        /*String gsonString = gson.toJson(bankUser);
        System.out.println(gsonString);
        BankUser bu = gson.fromJson(gsonString,BankUser.class);
        System.out.println(bu.getBirthday());*/
        this.postRequest(gson.toJson(bankUser),"/bank/user");
    }

}
