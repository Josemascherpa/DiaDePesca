package com.example.webscraping;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class NotificationsManager{
    Timer timerNotification = new Timer();
    private String CHANNEL_ID;
    private NotificationChannel canal;
    NotificationManager manager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CreateChannelNotification(String channel_id,CharSequence channel_name,String description ,Context context){
        CHANNEL_ID = channel_id;
        canal = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
        canal.enableLights(true);
        canal.setLightColor(Color.RED);
        canal.enableVibration(true);
        canal.setDescription(description);
        CreateNotificationManager(canal,context);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateNotificationManager(NotificationChannel canal,Context context){
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(manager != null) {
            manager.createNotificationChannel(canal);
        } else {
            Log.i("Error", "Notification manager is null");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SendNotify(String titleNotification, String textNotification, int minutes,Context context){
        timerNotification.schedule(new TimerTask() {
            @Override
            public void run() {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                builder.setSmallIcon(R.mipmap.ic_pesca);
                builder.setContentTitle(titleNotification);
                builder.setContentText(textNotification);
                builder.setAutoCancel(true);
                Notification notification = builder.build();

                manager.notify(1, notification);
            }
        },0,minutes* TimeUnit.MINUTES.toMillis(1));

    }
}
