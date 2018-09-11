package com.example.a91599.appname.DBHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.a91599.appname.Bean.NewsBean;
import java.util.ArrayList;
import java.util.List;


public class DBDao {
    private static final String ID = "id";//id自增长
    private static final String COMPANY_IMAGE = "company_image";
    private static final String TITLE = "title";
    private static final String SUMMARY = "summary";
    private static final String LINK = "link";
    private static final String FAVOURITE_COUNT = "favourite_count";
    private static final String STATUS = "status";
    private static final String CREATE_TIME = "create_time";
    private static final String UPDATE_TIME = "update_time";
    private static final String TABLE_NAME = "history_local";
    private DBHelper dbHelper;

    public DBDao(Context context) {
        dbHelper = new DBHelper((context));
    }

    public synchronized <T> void insert(T bean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (bean != null && bean instanceof NewsBean) {
                NewsBean newBean = (NewsBean) bean;
                ContentValues cv = new ContentValues();
                cv.put(COMPANY_IMAGE, newBean.getCompany_image());
                cv.put(TITLE, newBean.getTitle());
                cv.put(SUMMARY, newBean.getSummary());
                cv.put(LINK, newBean.getLink());
                cv.put(FAVOURITE_COUNT, newBean.getFavourite_count());
                cv.put(STATUS, newBean.getStatus());
                cv.put(CREATE_TIME, newBean.getCreatetime());
                cv.put(UPDATE_TIME, newBean.getUpdatetime());
                db.insert(TABLE_NAME, null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public synchronized void clearAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "delete from " + TABLE_NAME;
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public synchronized <T> List<T> query() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<T> list = new ArrayList<>();
        String querySql = "select * from " + TABLE_NAME;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(querySql, null);
            while (cursor.moveToNext()) {
                NewsBean newBean = new NewsBean();
                newBean.setCompany_image(cursor.getString(cursor.getColumnIndex(COMPANY_IMAGE)));
                newBean.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                newBean.setSummary(cursor.getString(cursor.getColumnIndex(SUMMARY)));
                newBean.setLink(cursor.getString(cursor.getColumnIndex(LINK)));
                newBean.setFavourite_count(cursor.getInt(cursor.getColumnIndex(FAVOURITE_COUNT)));
                newBean.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                newBean.setCreatetime(cursor.getLong(cursor.getColumnIndex(CREATE_TIME)));
                newBean.setUpdatetime(cursor.getLong(cursor.getColumnIndex(UPDATE_TIME)));
                list.add((T) newBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }



}
