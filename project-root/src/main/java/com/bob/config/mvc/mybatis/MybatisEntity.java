package com.bob.config.mvc.mybatis;

import java.time.LocalDate;

/**
 * Mybatis实体类
 *
 * @author wb-jjb318191
 * @create 2017-09-08 17:16
 */
@Table(key = "id")
public class MybatisEntity {

    @Column()
    private String id;
    @Column("USER_NAME")
    private String name;
    @Column()
    private Integer age;

    private LocalDate date;
    @Column("ADRESS_NUMBER")
    private Integer userAdressNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Column("CUR_DATE")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getUserAdressNumber() {
        return userAdressNumber;
    }

    public void setUserAdressNumber(Integer userAdressNumber) {
        this.userAdressNumber = userAdressNumber;
    }
}
