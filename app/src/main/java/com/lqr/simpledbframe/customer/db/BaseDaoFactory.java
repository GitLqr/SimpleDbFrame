package com.lqr.simpledbframe.customer.db;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * @创建者 CSDN_LQR
 * @描述 Dao类工厂(负责打开数据库，生产对应的表操作对象)
 */
public class BaseDaoFactory {

    private static String mDbPath;
    private SQLiteDatabase mDatabase;

    private static class Instance {
        public static BaseDaoFactory INSTANCE = new BaseDaoFactory();
    }

    public static BaseDaoFactory getInstance() {
        return Instance.INSTANCE;
    }

    // 初始化数据库位置
    public static void init(String dbPath) {
        mDbPath = dbPath;
    }

    public BaseDaoFactory() {
        if (TextUtils.isEmpty(mDbPath)) {
            throw new RuntimeException("在使用BaseDaoFactory之前，请调用BaseDaoFactory.init()初始化好数据库路径。");
        }
        // 打开数据库，得到数据库对象
        mDatabase = SQLiteDatabase.openOrCreateDatabase(mDbPath, null);
    }

    public <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entity) {
        T baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(mDatabase, entity);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }
}
