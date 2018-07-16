package com.android.mymemo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.android.mymemo.db.MemoDAOImpl;
import com.android.mymemo.entity.Memo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NtService extends Service {
    public NtService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        MemoDAOImpl mdi = new MemoDAOImpl(this);
        ArrayList<Memo> memos = mdi.getAllMemos(null,false);
        Date now = Calendar.getInstance().getTime();
        for (Memo m : memos){
            Date nDate = m.getNotificationDate();

            if (nDate.after(now)){
                Intent i = new Intent(this, AlarmReceiver.class);
                i.putExtra("memo_id",m.getId());
                i.putExtra("title",m.getTitle());
                String content = m.getContent();
                int clength = content.length();
                int length = clength > 20 ? 20 : clength;
                i.putExtra("content",content.substring(0,clength));
                Log.d("=====",nDate.toLocaleString());
                PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                if (Build.VERSION.SDK_INT >= 19){
                    manager.setExact(AlarmManager.RTC_WAKEUP, nDate.getTime(), pi);
                }else{
                    manager.set(AlarmManager.RTC_WAKEUP, nDate.getTime(), pi);
                }

            }
        }



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
    }
}
