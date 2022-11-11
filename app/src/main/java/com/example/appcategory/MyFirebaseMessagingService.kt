package com.example.appcategory

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.appcategory1"

class MyFirebaseMessagingService: FirebaseMessagingService() {

//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//        if (message.notification != null) {
//            generateNotification(message.notification!!.title.toString(),
//                message.notification!!.body.toString()
//            )
//        } else {
//            Toast.makeText(applicationContext, "cc", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun generateNotification(title: String, message: String) {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//
//        var builder = NotificationCompat.Builder(applicationContext, channelId)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setAutoCancel(false)
//            .setContentIntent(pendingIntent)
//
//        builder = builder.setContent(getRemoteView(title, message))
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//        notificationManager.notify(0, builder.build())
//    }
//
//    @SuppressLint("RemoteViewLayout")
//    private fun getRemoteView(title: String, message: String): RemoteViews {
//          val remoteView = RemoteViews("com.example.appcategory", R.layout.notification)
//        remoteView.setTextViewText(R.id.title, title)
//        remoteView.setTextViewText(R.id.message, message)
//        return remoteView
//    }
}