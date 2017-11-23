package com.lqr.simpledbframe.customer.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.lqr.simpledbframe.customer.db.annotation.TbField;
import com.lqr.simpledbframe.customer.db.annotation.TbName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class BaseDao<M> implements IBaseDao<M> {

    private SQLiteDatabase database;
    private Class<M> entityClass;
    private boolean inited = false;
    private String tableName = "";
    private Map<String, Field> fieldMap;

    protected boolean init(Class<M> entity, SQLiteDatabase database) {
        if (!inited) {
            synchronized (BaseDao.class) {
                if (!inited) {
                    this.database = database;
                    this.entityClass = entity;

                    // 判断数据库是否已经打开
                    if (!database.isOpen()) {
                        return false;
                    }

                    // 获取表名
                    TbName tbName = entity.getAnnotation(TbName.class);
                    tableName = tbName == null ? entity.getClass().getSimpleName() : tbName.value();

                    // 创建表
                    if (!TextUtils.isEmpty(createTable())) {
                        database.execSQL(createTable());
                    } else {
                        return false;
                    }

                    // 获取表字段与类字段映射
                    fieldMap = new HashMap<>();
                    initFieldMap();

                    inited = true;
                }
            }
        }
        return inited;
    }

    private void initFieldMap() {
        // 1、获取类中所有的字段，并设置为可访问
//        Field[] fields = entityClass.getFields();// 获得某个类的所有的公共（public）的字段，包括父类。
        Field[] fields = entityClass.getDeclaredFields();// 获得某个类的所有申明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
        for (Field field : fields) {
            field.setAccessible(true);
        }
        // 2、获取表中所有的字段，并与类字段比较（注意可能使用了字段注解）
        Cursor cursor = database.rawQuery("select * from " + tableName + " limit 1,0", null);
        if (cursor != null) {
            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                for (Field field : fields) {
                    TbField tbField = field.getAnnotation(TbField.class);
                    String cn = tbField == null ? field.getName() : tbField.value();
                    if (columnName.equals(cn)) {
                        fieldMap.put(columnName, field);
                        break;
                    }
                }
            }
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public long insert(M obj) {
        Map<String, String> values = getValues(obj);
        ContentValues cv = getContentValues(values);
        return database.insert(tableName, null, cv);
    }


    @Override
    public int remove(M where) {
        Map<String, String> whereMap = getValues(where);
        Condition condition = new Condition(whereMap);
        return database.delete(tableName, condition.whereCause, condition.whereArgs);
    }

    @Override
    public int update(M obj, M where) {
        Map<String, String> values = getValues(obj);
        ContentValues cv = getContentValues(values);
        Map<String, String> whereMap = getValues(where);
        Condition condition = new Condition(whereMap);
        return database.update(tableName, cv, condition.whereCause, condition.whereArgs);
    }

    @Override
    public List<M> query(M where) {
        return query(where, null, null, null);
    }

    @Override
    public List<M> query(M where, String orderBy) {
        return query(where, orderBy, null, null);
    }

    @Override
    public List<M> query(M where, String orderBy, Integer page, Integer pageCount) {
        List<M> result = new ArrayList<>();
        String limit = null;
        if (page != null && pageCount != null) {
            limit = (--page < 0 ? "0" : String.valueOf(page)) + "," + String.valueOf(pageCount);
        }
        Map<String, String> whereMap = getValues(where);
        Condition condition = new Condition(whereMap);
        Cursor cursor = database.query(tableName, null, condition.whereCause, condition.whereArgs, null, null, orderBy, limit);

        try {
            M item = (M) where.getClass().newInstance();
            while (cursor.moveToNext()) {
                Iterator<String> iterator = fieldMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String columnName = iterator.next();
                    int columnIndex = cursor.getColumnIndex(columnName);

                    if (columnIndex != -1) {
                        Field field = fieldMap.get(columnName);
                        Class<?> type = field.getType();
                        Object value = null;
                        if (type == String.class) {
                            value = cursor.getString(columnIndex);
                        } else if (type == int.class || type == Integer.class) {
                            value = cursor.getInt(columnIndex);
                        }
                        field.set(item, value);
                        result.add(item);
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return result;
    }

    /**
     * 将实体数据转成键值对
     */
    private Map<String, String> getValues(M obj) {
        Map<String, String> result = new HashMap<>();
        try {
            Iterator<Field> iterator = fieldMap.values().iterator();
            while (iterator.hasNext()) {
                Field colField = iterator.next();
                String colKey = "";
                TbField tbField = colField.getAnnotation(TbField.class);
                colKey = tbField == null ? colField.getName() : tbField.value();
                String colValue = null;
                Object o = colField.get(obj);
                if (o != null) {
                    colValue = o.toString();
                }
                result.put(colKey, colValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将键值对转成ContentValues
     */
    private ContentValues getContentValues(Map<String, String> values) {
        ContentValues cv = new ContentValues();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            cv.put(entry.getKey(), entry.getValue());
        }
        return cv;
    }

    class Condition {

        public Condition(Map<String, String> values) {
            StringBuffer sb = new StringBuffer();
            List<String> argList = new ArrayList<>();
            boolean first = true;
            for (Map.Entry<String, String> entry : values.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value != null) {
                    sb.append((first ? "" : " and ") + key + "=?");
                    argList.add(value);
                    first = false;
                }
            }
            this.whereCause = sb.toString();
            this.whereArgs = argList.toArray(new String[argList.size()]);
        }

        String whereCause;
        String[] whereArgs;
    }

    public abstract String createTable();
}
