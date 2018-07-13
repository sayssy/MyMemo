package com.android.mymemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "memo.db";
    private static final int VERSION = 1;

    /**
     * CREATE TABLE
     */
    private static final String SQL_CREATE_ACCOUNT_INFO = "create table accountInfo " +
            "(id text primary key," +
            "name text, password text," +
            "arrangement text, autoSync integer);";
    private static final String SQL_CREATE_MEMO = "create table memo " +
            "(id text primary key," +
            "accID text, title text, content text," +
            "createDate text, lastModifyDate text," +
            "notificationDate text, state integer);";

    private static final String SQL_DROP_ACCOUNT_INFO = "drop table if exists accountInfo";
    private static final String SQL_DROP_MEMO = "drop table if exists memo";


    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ACCOUNT_INFO);
        sqLiteDatabase.execSQL(SQL_CREATE_MEMO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_ACCOUNT_INFO);
        sqLiteDatabase.execSQL(SQL_CREATE_ACCOUNT_INFO);

        sqLiteDatabase.execSQL(SQL_DROP_MEMO);
        sqLiteDatabase.execSQL(SQL_CREATE_MEMO);
    }
}
