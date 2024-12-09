package com.example.team09finalgroupproject.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.team09finalgroupproject.R;

public class NotificationReceiver extends BroadcastReceiver {


    private static final String CHANNEL_ID = "CHANNEL_01";

    @Override
    public void onReceive(Context context, Intent intent) {
        String medicationName = intent.getStringExtra("medicationName");
        String message = intent.getStringExtra("message");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Medication Reminder")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
