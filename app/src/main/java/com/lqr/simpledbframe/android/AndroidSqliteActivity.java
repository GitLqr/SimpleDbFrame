package com.lqr.simpledbframe.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lqr.simpledbframe.R;

import java.util.List;

/**
 * @创建者 CSDN_LQR
 * @描述 安卓官方sqlite操作demo
 */
public class AndroidSqliteActivity extends AppCompatActivity {

    private UserDao mUserDao;
    private User user = new User("CSDN_LQR", "123456");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_sqlite);
        mUserDao = new UserDao(this);
    }

    public void add(View view) {
        long insert = mUserDao.insert(user);
        Toast.makeText(getApplicationContext(), "添加" + insert + "条数据", Toast.LENGTH_SHORT).show();
    }

    public void delete(View view) {
        int remove = mUserDao.remove(user.getUsername());
        Toast.makeText(getApplicationContext(), "删除" + remove + "条数据", Toast.LENGTH_SHORT).show();
    }

    public void change(View view) {
        user.setPassword("654321");
        int update = mUserDao.update(user);
        Toast.makeText(getApplicationContext(), "更新" + update + "条数据", Toast.LENGTH_SHORT).show();
    }

    public void get(View view) {
        List<User> userList = mUserDao.select();
        StringBuffer sb = new StringBuffer();
        for (User u : userList) {
            sb.append(u.toString());
        }
        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserDao.close();
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, AndroidSqliteActivity.class);
        context.startActivity(intent);
    }
}
