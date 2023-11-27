package com.teamx.hatly

import android.content.Context
import android.util.Log
import com.teamx.hatly.ui.activity.mainActivity.MainActivity.Companion.service
import org.json.JSONException
import org.json.JSONObject

object NotificationHelper {
//    fun displayNotification(context: Context?, title: String?, description: String) {
//       /* val builder = NotificationCompat.Builder(
//            context!!, "MainActivity.Channel_ID"
//        )
//            .setSmallIcon(R.drawable.ic_raseef)
//            .setContentTitle(title)
//            .setContentText(description)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
//        builder.setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
//        val notificationManagerCompat = NotificationManagerCompat.from(
//            context
//        )
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        notificationManagerCompat.notify(1, builder.build())*/
//        Log.d("showNotification1", "description: ${description}")
//        try {
//            val jsonObject = JSONObject(description)
//
//            val type = jsonObject.getString("type")
//            val descriptionJson = jsonObject.getString("Descripion") // Note the typo in the JSON key
//            val orderID = jsonObject.getString("orderID")
//            val actionText = jsonObject.getString("actionText")
//
//            service!!.showNotification1(title!!, descriptionJson, type, orderID)
//        }catch (e: JSONException) {
//            e.printStackTrace()
//            service!!.showNotification1(title!!, description/*e.message.toString()*/)
//        }
//
//    }


    fun displayNotification(context: Context?, title: String?, description: String) {

        Log.d("showNotification1", "description: ${description}")
        try {
            val jsonObject = JSONObject(description)

            val type = jsonObject.getString("type")
            val descriptionJson = jsonObject.getString("Descripion") // Note the typo in the JSON key
            val orderID = jsonObject.getString("orderID")
            val actionText = jsonObject.getString("actionText")

            service!!.showNotification1(title!!, descriptionJson, type, orderID)
        }catch (e: JSONException) {
            e.printStackTrace()
            Log.d("showNotification1", "description JSONException: ${description}")
            Log.d("showNotification1", "title JSONException: ${title}")
            service!!.showNotification1(title!!, description, "dummy soahil","46545646")
        }catch (e : Exception){
            Log.d("showNotification1", "description: Exception ${description}")
        }

    }

}