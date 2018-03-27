package com.bob.web.concrete.controller;

import java.util.Calendar;
import java.util.Date;

import com.bob.web.config.BaseControllerTest;
import com.bob.web.mvc.controller.BankController;
import com.bob.web.mvc.entity.model.BankUser;
import com.google.gson.Gson;
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
    public void testCreateUser() {
        BankUser bankUser = new BankUser();
        bankUser.setEmail("q450210669@163.com");
        bankUser.setSex(1);
        bankUser.setIdcard("330881198807262314");
        bankUser.setPhoneNumber("18758107760");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1988, 6, 26);
        bankUser.setBirthday(calendar.getTime());
        bankUser.setUsername("JiangJibo");
        bankUser.setAdress("杭州余杭");
        bankUser.setAge(29);
        this.postRequest(gson.toJson(bankUser), "/bank/user");
    }

    @Test
    public void testGetById() {
        String result = this.getRequest("/bank/user/12");
        System.out.println(result);
    }

    @Test
    public void testGetByIds() {
        //String result = this.getRequest("/bank/user/ids?ids=1587&ids=4875&ids=256");
        String result = this.getRequest("/bank/user/ids?ids=1587,4875,256");
        System.out.println(result);
    }

    @Test
    public void testGetByAgeForPages() {
        String result = this.getRequest("/user/age/29?currentPage=3&size=10");
        System.out.println(result);
    }

    public void testToJson() {
        System.out.println(new Gson().toJson(new Date()));
    }

    @Test
    public void testFromJson() {
        Gson gson = new Gson();
        Date date = gson.fromJson("Dec 5, 2017 8:03:34 PM", Date.class);
        System.out.println(date);
    }

    @Test
    public void testRequestPermitGenerate() {
        String result = this.getRequest("/bank/adminmap/api?currentPage=3&size=10&ids=1587&ids=4875&ids=256");
        System.out.println(result);
    }

}
