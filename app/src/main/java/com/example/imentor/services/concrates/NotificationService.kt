package com.example.imentor.services.concrates

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.imentor.App
import com.example.imentor.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("FCM_TOKEN", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            val title = it.title
            val body = it.body
            Log.i("DATA_MESSAGE", body.toString())
            val notificationManager = getSystemService(NotificationManager::class.java)
            val notification = NotificationCompat.Builder(this, App.FCM_CHANNEL_ID)
                .setContentTitle(title.toString())
                .setContentText(title.toString())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

            notificationManager.notify(1002, notification)
        }

       // Log.i("DATA_MESSAGE", message.data.toString())
    }
}