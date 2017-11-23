package com.lqr.simpledbframe.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public static class T {

        public final static String NAME = "t_user";

        public static class Column {
            public final static String USERNAME = "username";
            public final static String PASSWORD = "password";
        }

        public static void createTable(SQLiteDatabase db) {
            db.execSQL("create table if not exists " + NAME
                    + "(" + Column.USERNAME + " varchar(20),"
                    + Column.PASSWORD + "  varchar(10))");
        }
    }

    private MySQLiteOpenHelper mOpenHelper;
    private SQLiteDatabase mDatabase;

    public UserDao(Context context) {
        mOpenHelper = new MySQLiteOpenHelper(context);
        mDatabase = mOpenHelper.getWritableDatabase();
    }

    public long insert(User user) {
        ContentValues cv = new ContentValues();
        cv.put(T.Column.USERNAME, user.getUsername());
        cv.put(T.Column.PASSWORD, user.getPassword());
        return mDatabase.insert(T.NAME, null, cv);
    }

    public int remove(String username) {
        return mDatabase.delete(T.NAME, T.Column.USERNAME + " = ? ", new String[]{username});
    }

    public int update(User user) {
        ContentValues cv = new ContentValues();
        cv.put(T.Column.USERNAME, user.getUsername());
        cv.put(T.Column.PASSWORD, user.getPassword());
        return mDatabase.update(T.NAME, cv, T.Column.USERNAME + " = ? ", new String[]{user.getUsername()});
    }

    public List<User> select() {
        List<User> list = null;
        Cursor cursor = null;
        try {
            cursor = mDatabase.query(T.NAME, null, null, null, null, null, null, null);
            list = new ArrayList<>();
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(T.Column.USERNAME));
                String password = cursor.getString(cursor.getColumnIndex(T.Column.PASSWORD));
                User user = new User(username, password);
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return list;
    }

    public void close() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
        if (mOpenHelper != null) {
            mOpenHelper.close();
            mOpenHelper = null;
        }
    }
}
