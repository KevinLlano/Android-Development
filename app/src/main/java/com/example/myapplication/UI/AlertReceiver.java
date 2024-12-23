package com.example.myapplication.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;

public class AlertReceiver extends BroadcastReceiver {
    String channel_id = "vacation_alert_channel";
    static int notificationID = 1;

    // BroadcastReceiver for alerts, messages, notifications, etc.
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            createNotificationChannel(context, channel_id);
            Notification n = new NotificationCompat.Builder(context, channel_id)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(message)
                    .setContentTitle("Excursion Alert")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID++, n);
        }
    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        CharSequence name = "Vacation Alerts";
        String description = "Channel for Vacation Alerts";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
