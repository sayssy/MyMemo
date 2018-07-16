package com.android.mymemo.utility;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.MediaCodec;
import android.os.Build;
import android.os.VibrationEffect;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.mymemo.R;
import com.android.mymemo.activity.MemoInfoActivity;

import java.nio.channels.Channel;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";
    private static int notid = 1;

    public NotificationUtils(Context context){
        super(context);
    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI,Notification.AUDIO_ATTRIBUTES_DEFAULT);
            channel.setLightColor(Notification.COLOR_DEFAULT);
            channel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
            getManager().createNotificationChannel(channel);
        }
    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    public Notification.Builder getChannelNotification(String title, String content){
        if (Build.VERSION.SDK_INT >= 26) {
            return new Notification.Builder(getApplicationContext(), id)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.logo_round)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo_round))
                    .setAutoCancel(true);

        }
        return null;
    }
    public NotificationCompat.Builder getNotification_25(String title, String content){
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.logo_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo_round))
                .setAutoCancel(true);
    }
    public void sendNotification(String title, String content,String memo_id){
        Intent intent = new Intent(this, MemoInfoActivity.class);
        intent.putExtra("function","update");
        intent.putExtra("memo",memo_id);

        PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT>=26){
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content).build();
            notification.tickerText = "提醒！";
            notification.defaults = Notification.DEFAULT_ALL;
            notification.contentIntent = pintent;
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            getManager().notify(notid++,notification);
        }else{
            Notification notification = getNotification_25(title, content).build();
            notification.tickerText = "提醒！";
            notification.defaults = Notification.DEFAULT_ALL;
            notification.contentIntent = pintent;
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            getManager().notify(notid++,notification);
        }
    }
}
