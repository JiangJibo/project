package com.bob.root.config.entity;

/**
 * 实体类模板
 *
 * @author wb-jjb318191
 * @create 2018-02-06 14:57
 */
public class RootUser {

    private Integer id;
    private String name;
    private Integer age;
    private String adress;
    private String password;
    private String telephone;

    public RootUser() {
    }

    public RootUser(String userName, String password) {
        this.name = userName;
        this.password = password;
    }

    public RootUser(Integer id, String userName, String password) {
        this.id = id;
        this.name = userName;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
