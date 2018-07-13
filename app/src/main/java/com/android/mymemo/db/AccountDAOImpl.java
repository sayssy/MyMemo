package com.android.mymemo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.mymemo.entity.Account;
import com.android.mymemo.entity.AccountInfo;

import java.text.ParseException;

public class AccountDAOImpl implements AccountInfoDAO {

    private DataBaseHelper helper = null;
    public AccountDAOImpl(Context context) {
        this.helper = new DataBaseHelper(context);
    }


    @Override
    public void insertAccountInfo(AccountInfo accountInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlStatement= "insert into accountInfo(id, name, password, arrangement, autoSync) values (?, ?, ?, ?, ?)";
        db.execSQL(sqlStatement,
                new Object[] {accountInfo.getAccount().getId(), accountInfo.getAccount().getName(),
                        accountInfo.getAccount().getPassword(), accountInfo.getArrangement(),
                        accountInfo.getAutoSync()});
        db.close();
        Log.d("dao", "enter");
    }

    @Override
    public void updateAccountInfo(AccountInfo accountInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlStatement= "update accountInfo set name = ?, password = ?, " +
                "arrangement = ?, autoSync = ? where id = ?";
        db.execSQL(sqlStatement,
                new Object[] {accountInfo.getAccount().getName(), accountInfo.getAccount().getPassword(),
                        accountInfo.getArrangement(), accountInfo.getAutoSync(),
                        accountInfo.getAccount().getId()});
        db.close();
    }

    @Override
    public void deleteAccountInfo() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlStatement= "delete from accountInfo";
        db.execSQL(sqlStatement);
        db.close();
    }

    @Override
    public boolean isExisted() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlQueryStatement= "select * from accountInfo";
        Cursor cursors = db.rawQuery(sqlQueryStatement,null);
        boolean exists = cursors.moveToNext();
        cursors.close();
        db.close();
        return exists;
    }

    @Override
    public AccountInfo getAccountInfo() {
        Account account;
        AccountInfo accountInfo = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlQueryStatement= "select * from accountInfo";
        Cursor cursors = db.rawQuery(sqlQueryStatement, null);
        if(cursors.moveToNext()) {
            account = new Account();
            accountInfo = new AccountInfo();
            account.setId(cursors.getString(cursors.getColumnIndex("id")));
            account.setName(cursors.getString(cursors.getColumnIndex("name")));
            account.setPassword(cursors.getString(cursors.getColumnIndex("password")));
            accountInfo.setAccount(account);

            accountInfo.setArrangement(cursors.getString(cursors.getColumnIndex("arrangement")));
            accountInfo.setAutoSync(cursors.getInt(cursors.getColumnIndex("autoSync")));
        }
        cursors.close();
        db.close();
        return accountInfo;
    }
}
