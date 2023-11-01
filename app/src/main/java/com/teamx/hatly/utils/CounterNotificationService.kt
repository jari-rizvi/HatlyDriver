package com.example.payhilt.utils

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.teamx.hatly.PushNotificationService
import com.teamx.hatly.ui.activity.mainActivity.MainActivity


class CounterNotificationService(
    private val context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(counter: Int) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val incrementIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, CounterNotificationReceiver::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_menu_agenda)
            .setContentTitle("Increment counter")
            .setContentText("The count is $counter")
            .setContentIntent(activityPendingIntent)
            .addAction(
                R.drawable.ic_menu_agenda,
                "Increment",
                incrementIntent
            )
            .build()

        notificationManager.notify(1, notification)
    }

    fun showNotification1(header: String, comment: String) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
         val incrementIntent = PendingIntent.getBroadcast(
             context,
             2,
             Intent(context, PushNotificationService::class.java),
             if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
         )
        Log.d("showNotification1", "showNotification1: ${comment}")
        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_menu_agenda)
            .setContentTitle(header)
            .setContentText(comment)
            .setContentIntent(activityPendingIntent)
            .addAction(
              R.drawable.ic_menu_agenda,
                "View",
                incrementIntent
            )
            .build()

        notificationManager.notify(1, notification)
    }

    fun showNotification1(header: String, comment: String, type: String, orderID: String) {
        val activityIntent = Intent(context, MainActivity::class.java)

        Log.d("orderHistoryFragment", "onCreate 1: $type")

//        val bundle = Bundle()
//
//        bundle.putString("typeDir",type)

        activityIntent.putExtra("typeDir", type)

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )



//        val activityPendingIntent = PendingIntent.getActivity(
//            context,
//            1,
//            activityIntent,
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0,bundle
//        )


        val incrementIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, PushNotificationService::class.java),
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        Log.d("showNotification1", "showNotification122 : ${comment}")
        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_menu_agenda)
            .setContentTitle(header)
            .setContentText(comment)
            .setContentIntent(activityPendingIntent)
            .addAction(R.drawable.ic_menu_agenda,
                "View",
                incrementIntent
            )
            .build()

        notificationManager.notify(1, notification)
    }


    companion object {
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}