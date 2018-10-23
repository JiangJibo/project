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

    private List<String> addresses;

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

}
