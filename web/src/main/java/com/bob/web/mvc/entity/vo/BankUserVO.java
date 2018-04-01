package com.bob.web.mvc.entity.vo;

import java.util.List;

import com.bob.web.mvc.entity.model.BankUser;

/**
 * 银行用户视图对象
 *
 * @author wb-jjb318191
 * @create 2018-01-24 17:38
 */
public class BankUserVO extends BankUser {

    private List<String> adresses;

    public List<String> getAdresses() {
        return adresses;
    }

    public void setAdresses(List<String> adresses) {
        this.adresses = adresses;
    }

}
