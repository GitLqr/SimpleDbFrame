package com.lqr.simpledbframe.customer.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

public class BaseDaoFactory {

    private static BaseDaoFactory mInstance;
    private static String mDbPath;
    private SQLiteDatabase mSqLiteDatabase;
    private Map<String, BaseDao> mBaseDaoMap;

    /**
     * 初始化数据库路径（建议在Application中调用）
     */
    public static void init(String dbPath) {
        mDbPath = dbPath;
    }

    private BaseDaoFactory() {
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(mDbPath, null);
        mBaseDaoMap = new HashMap<>();
    }

    public static BaseDaoFactory getInstance() {
        if (mInstance == null) {
            synchronized (BaseDaoFactory.class) {
                if (mInstance == null) {
                    mInstance = new BaseDaoFactory();
                }
            }
        }
        return mInstance;
    }

    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entity) {
//        if (mBaseDaoMap.get(clazz.getName()) != null) {
//            return (T) mBaseDaoMap.get(clazz.getName());
//        }
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entity, mSqLiteDatabase);
//            mBaseDaoMap.put(clazz.getName(), baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

}
