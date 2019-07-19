package com.bob.web.mvc.controller;

import com.bob.common.utils.request.GetRequestParams;
import com.bob.web.mvc.entity.model.BankUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wb-jjb318191
 * @create 2019-07-18 19:48
 */
@RestController
@RequestMapping("/getrequest")
public class RequestParamWrapperController {

    @GetMapping
    public void warpParams(@GetRequestParams BankUser bankUser) {
        System.out.println(bankUser.getAddress());
    }

}
