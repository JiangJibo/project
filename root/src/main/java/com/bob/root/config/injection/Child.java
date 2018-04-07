/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.root.config.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * 想要知道直接问,不好解释
 *
 * @since 2017年6月6日 下午2:37:53
 * @version $Id$
 * @author JiangJibo
 *
 */
// @Component
@Scope("prototype")
// @Scope(value = "request")
public class Child {

    private String name;
    private int age;
    private String sex;

    public Child() {
        this.name = "小明";
        this.age = 18;
        this.sex = "男";
    }

    @Autowired(required = false)
    public Child(Mother mother, Father father) {
        this.name = mother.getName() + father.getName();
        this.age = father.getAge() - 25;
        this.sex = "男";
    }

    @Autowired(required = false)
    public Child(String name, int age, String sex) {
        super();
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     *            the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

}
