package com.lqr.simpledbframe.customer;

import com.lqr.simpledbframe.customer.db.annotation.TbField;
import com.lqr.simpledbframe.customer.db.annotation.TbName;

@TbName("t_user")
public class User {

    @TbField("tb_name")
    private String username;
    @TbField("tb_password")
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "[" + this.username + " , " + this.password + "]\n";
    }
}
