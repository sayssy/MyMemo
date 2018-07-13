package com.android.mymemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "memo.db";
    private static final int VERSION = 1;
    private static final String SQL_CREATE = "create table memo " +
            "(id text primary key," +
            "accID text, title text, content text," +
            "createDate text, lastModifyDate text," +
            "notificationDate text, state integer);";
    private static final String SQL_DROP = "drop table if exists memo";
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP);
        sqLiteDatabase.execSQL(SQL_CREATE);
    }
}
