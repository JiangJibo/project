package com.bob.mvc.controller;

import com.bob.mvc.model.BankUser;
import com.bob.mvc.service.BankUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 银行控制器
 *
 * @author wb-jjb318191
 * @create 2017-12-05 14:01
 */
@RestController()
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankUserService bankUserService;

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public boolean createUser(@RequestBody BankUser bankUser){
        return bankUserService.create(bankUser);
    }

}
