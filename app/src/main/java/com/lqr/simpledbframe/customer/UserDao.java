package com.lqr.simpledbframe.customer;

import com.lqr.simpledbframe.customer.db.BaseDao;

public class UserDao extends BaseDao<User> {

    @Override
    public String createTable() {
        return "create table if not exists t_user(tb_name varchar(20),tb_password varchar(10))";
    }
}
