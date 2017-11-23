package com.lqr.simpledbframe.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lqr.simpledbframe.R;
import com.lqr.simpledbframe.customer.db.BaseDaoFactory;

import java.util.List;

/**
 * @创建者 CSDN_LQR
 * @描述 自定义数据库框架
 */
public class CustomerDbFrameActivity extends AppCompatActivity {

    private UserDao mUserDao;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_db_frame);
        mUserDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
        mUser = new User("CSDN_LQR", "123456");
    }

    public void add(View view) {
        long insert = mUserDao.insert(mUser);
        Toast.makeText(getApplicationContext(), "添加" + insert + "条数据", Toast.LENGTH_SHORT).show();
    }

    public void delete(View view) {
        User where = new User();
        where.setUsername("LQR_CSDN");
        int remove = mUserDao.remove(where);
        Toast.makeText(getApplicationContext(), "删除" + remove + "条数据", Toast.LENGTH_SHORT).show();
    }

    public void change(View view) {
        User user = new User("LQR_CSDN", "654321");
        User where = new User();
        where.setUsername("CSDN_LQR");
        int update = mUserDao.update(user, where);
        Toast.makeText(getApplicationContext(), "更新" + update + "条数据", Toast.LENGTH_SHORT).show();
    }

    public void get1(View view) {
        User where = new User();
        where.setUsername("LQR_CSDN");
        List<User> list = mUserDao.query(where);
        StringBuilder sb = new StringBuilder();
        for (User user : list) {
            sb.append(user.toString());
        }
        System.out.println(sb.toString());
    }

    public void get2(View view) {
        User where = new User();
        where.setUsername("LQR_CSDN");
        List<User> list = mUserDao.query(where, "tb_name desc");
        StringBuilder sb = new StringBuilder();
        for (User user : list) {
            sb.append(user.toString());
        }
        System.out.println(sb.toString());
    }

    public void get3(View view) {
        User where = new User();
        where.setUsername("LQR_CSDN");
        List<User> list = mUserDao.query(where, "tb_name desc", 1, 10);
        StringBuilder sb = new StringBuilder();
        for (User user : list) {
            sb.append(user.toString());
        }
        System.out.println(sb.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, CustomerDbFrameActivity.class);
        context.startActivity(intent);
    }
}
