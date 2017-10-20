package com.example.vamsi.firebaseauth1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by vamsi on 24/5/17.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        int count ;
        Bundle b = intent.getExtras();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent(context, CouponLayoutSingle.class);

        i.putExtra("name",b.getString("name"));
        i.putExtra("terms",b.getString("terms"));
        i.putExtra("url",b.getString("url"));
        i.putExtra("couponID",b.getString("couponID"));

        count = b.getInt("count")*10;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, count, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.check)
                .setContentTitle("Coupon Reminder !")
                .setContentText(b.getString("name"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(count, builder.build());
    }

}
