package com.bob.intergration.concrete.hession2;

import java.io.Serializable;

/**
 * Hession2测试实体类
 *
 * @author wb-jjb318191
 * @create 2018-07-26 9:18
 */
public class Hession2Entity implements Serializable {

    private static final long serialVersionUID = 3631501199413274104L;

    private Long id;

    private String name;

    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
