package com.android.mymemo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.android.mymemo.entity.Memo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemoDAOImpl implements MemoDAO {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DataBaseHelper helper = null;
    public MemoDAOImpl(Context context) {
        this.helper = new DataBaseHelper(context);
    }

    @Override
    public void insertMemo(Memo memo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlStatement= "insert into memo(id, accID, title, content," +
                " createDate, lastModifyDate, notificationDate, state) values (?, ?, ?, ?, ?, ?, ?, ?)";
        db.execSQL(sqlStatement,
                new Object[] {memo.getId(), memo.getAccID(),
                        memo.getTitle(), memo.getContent(),
                        sdf.format(memo.getCreateDate()).toString(), sdf.format(memo.getLastModifyDate()).toString(),
                        sdf.format(memo.getNotificationDate()).toString(), memo.getState()});
        db.close();
    }

    @Override
    public void deleteMemo(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlStatement= "update memo set state = 0 where id = ?";
        db.execSQL(sqlStatement, new String[] {id});
        db.close();
    }

    @Override
    public void deleteAllMemos() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlStatement= "delete from memo ";
        db.execSQL(sqlStatement, new String[] {});
        db.close();
    }

    @Override
    public void updateMemo(Memo memo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlStatement= "update memo set title = ?, content = ?, lastModifyDate = ? where id = ?";
        db.execSQL(sqlStatement,
                new Object[] {memo.getTitle(), memo.getContent(),
                        sdf.format(memo.getLastModifyDate()).toString(), memo.getId()});
        db.close();
    }

    @Override
    public Memo getSingleMemo(String id) {
        Memo memo = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlQueryStatement= "select * from memo where id = ?";
        Cursor cursors = db.rawQuery(sqlQueryStatement, new String[] {id});
        if(cursors.moveToNext()) {
            memo = new Memo();
            memo.setId(cursors.getString(cursors.getColumnIndex("id")));
            memo.setAccID(cursors.getString(cursors.getColumnIndex("accID")));
            memo.setTitle(cursors.getString(cursors.getColumnIndex("title")));
            memo.setContent(cursors.getString(cursors.getColumnIndex("content")));
            try {
                memo.setCreateDate(sdf.parse(cursors.getString(cursors.getColumnIndex("createDate"))));
                memo.setLastModifyDate(sdf.parse(cursors.getString(cursors.getColumnIndex("lastModifyDate"))));
                memo.setNotificationDate(sdf.parse(cursors.getString(cursors.getColumnIndex("notificationDate"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            memo.setState(cursors.getInt(cursors.getColumnIndex("state")));
        }
        cursors.close();
        db.close();
        return memo;
    }

    @Override
    public ArrayList<Memo> getAllMemos(String sort_way) {
        String sort_sql = "";

        if (sort_way != null){
            if (sort_way.equals("M")){
                sort_sql = " order by date(lastModifyDate) desc";
            }else if (sort_way.equals("C")){
                sort_sql = " order by date(createDate) desc";
            }
        }

        ArrayList<Memo> list = new ArrayList<Memo>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlQueryStatement= "select * from memo where state = 1" + sort_sql;
        Cursor cursors = db.rawQuery(sqlQueryStatement, new String[]{});
        while (cursors.moveToNext()) {
            Memo memo = new Memo();
            memo.setId(cursors.getString(cursors.getColumnIndex("id")));
            memo.setAccID(cursors.getString(cursors.getColumnIndex("accID")));
            memo.setTitle(cursors.getString(cursors.getColumnIndex("title")));
            memo.setContent(cursors.getString(cursors.getColumnIndex("content")));
            try {
                memo.setCreateDate(sdf.parse(cursors.getString(cursors.getColumnIndex("createDate"))));
                memo.setLastModifyDate(sdf.parse(cursors.getString(cursors.getColumnIndex("lastModifyDate"))));
                memo.setNotificationDate(sdf.parse(cursors.getString(cursors.getColumnIndex("notificationDate"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            memo.setState(cursors.getInt(cursors.getColumnIndex("state")));
            list.add(memo);
        }
        cursors.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExists(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlQueryStatement= "select * from memo where id = ?";
        Cursor cursors = db.rawQuery(sqlQueryStatement, new String[] {id});
        boolean exists = cursors.moveToNext();
        cursors.close();
        db.close();
        return exists;
    }

    @Override
    public void setNotificationDate(String id, Date date) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlStatement= "update memo set notificationDate = ? where id = ?";
        db.execSQL(sqlStatement,new Object[] {sdf.format(date.toString()), id});
        db.close();
    }
}
