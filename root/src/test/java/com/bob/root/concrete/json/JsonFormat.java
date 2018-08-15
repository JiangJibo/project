package com.bob.root.concrete.json;

import java.util.List;

/**
 * @author wb-jjb318191
 * @create 2018-08-08 10:58
 */
public class JsonFormat {

    /**
     * id : 0
     * name : aaa
     * users : [{"id":9,"name":"QA测试12","user":null},{"id":21,"name":"鱿鱼","user":null}]
     */

    private int id;
    private String name;
    private List<RootUser> users;

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public List<RootUser> getUsers() { return users;}

    public void setUsers(List<RootUser> users) { this.users = users;}

    public static class RootUser {

        /**
         * id : 9
         * name : QA测试12
         * user : null
         */

        private int id;
        private String name;

        public int getId() { return id;}

        public void setId(int id) { this.id = id;}

        public String getName() { return name;}

        public void setName(String name) { this.name = name;}
    }
}
