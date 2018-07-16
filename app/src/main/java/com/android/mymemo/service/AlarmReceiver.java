package com.android.mymemo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.android.mymemo.R;
import com.android.mymemo.utility.NotificationUtils;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String memo_id = intent.getStringExtra("memo_id");
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.sendNotification(title, content,memo_id);


    }


}
