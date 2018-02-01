package com.bob.project.config.mvc.model;

import java.io.Serializable;

import com.bob.project.utils.excelmapping.ExcelColumn;
import com.bob.project.utils.excelmapping.ExcelColumn.Column;
import com.bob.project.utils.excelmapping.ExcelMapping;
import com.bob.project.utils.excelmapping.PropertyInitializer;
import com.bob.project.mvc.entity.model.BankUser;

/**
 * Excel映射的Model
 *
 * @author dell-7359
 * @create 2017-10-19 19:46
 */
@ExcelMapping(titleRow = 0, dataRow = 1)
public class ExcelModel implements Serializable, PropertyInitializer<ExcelModel> {

    private static final long serialVersionUID = -422292418932719315L;

    private Integer id;

    private String userName;

    private String password;

    private Integer age;

    private String telephone;

    private String adress;

    @ExcelColumn(Column.A)
    private BankUser user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public BankUser getUser() {
        return user;
    }

    public void setUser(BankUser user) {
        this.user = user;
    }

    @Override
    public ExcelModel initProperties() {
        return new ExcelModel();
    }
}
