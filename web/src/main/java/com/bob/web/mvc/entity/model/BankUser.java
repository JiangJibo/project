package com.bob.web.mvc.entity.model;

import java.util.Date;

import com.bob.common.utils.validate.Group;
import com.bob.common.utils.validate.constraints.Email;
import com.bob.common.utils.validate.constraints.MaxLength;
import com.bob.common.utils.validate.constraints.NotNull;
import com.bob.common.entity.base.BaseModel;

/**
 * 数据库表：bank_user
 *
 * @author dell-7359
 * @create 2017-10-24
 */
public class BankUser extends BaseModel {

    /**
     * 银行用户主键
     */
    private Integer userId;

    /**
     * 银行用户名称
     */
    @NotNull
    @MaxLength(value = 50, group = Group.A)
    private String username;

    /**
     * 银行用户生日
     */
    @NotNull(group = Group.B)
    private Date birthday;

    /**
     * 银行用户地址
     */
    private String address;

    /**
     * 银行用户年龄
     */
    private Integer age;

    /**
     * 银行用户性别
     */
    private Integer sex;

    /**
     * 银行用户手机号码
     */
    private String phoneNumber;

    /**
     * 银行用户邮箱
     */
    @Email
    private String email;

    /**
     * 银行用户身份证号码
     */
    private String idcard;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }
}