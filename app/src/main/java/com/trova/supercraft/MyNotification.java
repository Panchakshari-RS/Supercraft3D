package com.trova.supercraft;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


/**
 * Created by Panchakshari on 27/1/2016.
 */
public class MyNotification extends Notification {
    Bitmap largeIcon;
    private String notificationTitle = "Supercraft Notification";
    public static final String TAG = "MyNotification ->";

    public MyNotification(Context ctx, String notificationText, String title, String productId){
        super();
        int NOTIFY_ID = 123456;
        Notification notification = null;

        if(title!=null){
            notificationTitle=title;
        }

        largeIcon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logo);

        Intent myIntent = new Intent(ctx, MyBroadCastReceiver.class);
        myIntent.putExtra("Supercraft_Notification", NOTIFY_ID);
        if(productId != null) {
            Bundle jobInfo = new Bundle();
            if(jobInfo != null) {
                jobInfo.putString("callerName", notificationText);
                jobInfo.putString("callerPhone", title);
                jobInfo.putString("productId", productId);
                myIntent.putExtra("jobInfo", jobInfo);
            }
        }
        myIntent.setAction("OpenChatPage");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, (int) System.currentTimeMillis(), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification = new Builder(ctx)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setTicker("And more")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public MyNotification(Context ctx, String notificationText, String jobId){
        super();
        int NOTIFY_ID = 123456;
        Notification notification = null;

        if(jobId!=null){
            notificationTitle= "Job : #00000" + jobId;
        }

        largeIcon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logo);

        Intent myIntent = new Intent(ctx, MyBroadCastReceiver.class);
        myIntent.putExtra("Supercraft_Notification", NOTIFY_ID);
        if(jobId != null) {
            myIntent.putExtra("jobId", jobId);
        }
        myIntent.setAction("OpenNotificationPage");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, (int) System.currentTimeMillis(), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification = new Builder(ctx)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setTicker("And more")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

}
