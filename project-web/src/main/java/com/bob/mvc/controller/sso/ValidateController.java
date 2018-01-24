package com.bob.mvc.controller.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证控制器
 *
 * @author wb-jjb318191
 * @create 2018-01-24 16:36
 */
@RestController
@RequestMapping("/sso")
public class ValidateController {

    @RequestMapping("verify")
    public JSONObject verify(HttpServletRequest request, String token) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        if (session.getAttribute("token") != null && token.equals(session.getAttribute("token"))) {
            result.put("code", "success");
            result.put("msg", "认证成功");
        } else {
            result.put("code", "failure");
            result.put("msg", "token已失效，请重新登录！");
        }
        return result;
    }
}
