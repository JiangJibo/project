package com.bob.web.mvc.controller;

import com.bob.common.utils.request.get.GetRequestParams;
import com.bob.common.utils.request.post.entity.HttpBody;
import com.bob.common.utils.request.post.entity.RiskDataParam;
import com.bob.web.mvc.entity.model.BankUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wb-jjb318191
 * @create 2019-07-18 19:48
 */
@RestController
@RequestMapping("/request")
public class RequestParamWrapperController {

    @GetMapping
    public void warpParams(@GetRequestParams BankUser bankUser) {
        System.out.println(bankUser.getAddress());
    }

    /**
     * 消息数据上报
     *
     * @param dataParam
     * @return
     */
    @PostMapping
    public RiskDataParam dataReport(@HttpBody RiskDataParam dataParam) {
        return dataParam;
    }

}
