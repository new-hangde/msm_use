package com.example.a91599.appname.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



 class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "recruit.db";
    private static final int DB_VERSION = 1;
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


    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
                ID + " integer primary key autoincrement," +
                COMPANY_IMAGE + " text," +
                TITLE + " integer," +
                SUMMARY + " text," +
                LINK + " text," +
                FAVOURITE_COUNT + " integer," +
                STATUS + " text," +
                CREATE_TIME + " integer," +
                UPDATE_TIME + " integer" +
                ")";
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
