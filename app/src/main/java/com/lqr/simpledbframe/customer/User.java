package com.lqr.simpledbframe.customer;

import com.lqr.simpledbframe.customer.db.anno.TbField;
import com.lqr.simpledbframe.customer.db.anno.TbName;

@TbName("tb_user")
public class User {

    @TbField(value = "tb_name", length = 30)
    private String username;

    @TbField(value = "tb_password", length = 20)
    private String password;

    @TbField(value = "tb_age", length = 11)
    private Integer age;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, int age) {
        this.username = username;
        this.password = password;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public String toString() {
        return "[username:" + this.username + ", password:" + this.getPassword() + ", age:" + this.getAge() + "]";
    }
}
