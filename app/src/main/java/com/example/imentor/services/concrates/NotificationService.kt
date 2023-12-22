package com.example.imentor.services.concrates

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.example.imentor.App
import com.example.imentor.R
import com.example.imentor.main.entities.INotification
import com.example.imentor.main.entities.Task
import com.example.imentor.main.services.concrates.TaskManager
import com.example.imentor.util.SharedPreferencesUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    private val notificationFirestore = com.example.imentor.main.services.concrates.NotificationManager()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("FCM_TOKEN", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val allowNotifications = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications", true)
        Log.i("cloud", allowNotifications.toString())
        if (allowNotifications) {
            message.notification?.let {
                val title = it.title
                val body = it.body
                Log.i("DATA_MESSAGE", body.toString())
                val builder = NotificationCompat.Builder(this, App.FCM_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle(title.toString())
                    .setContentText(body.toString())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Create a notification manager object
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                // Check if the device is running Android Oreo or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Create a notification channel
                    val channel = NotificationChannel(
                        App.FCM_CHANNEL_ID,
                        "test",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    channel.description = "test"
                    // Register the channel with the system
                    notificationManager.createNotificationChannel(channel)
                }
                // To see the message in logcat
                Log.i("Notify", "$builder")
                // Issue the notification
                notificationManager.notify(1, builder.build())
                val not = INotification(
                    body = body.toString(),
                    title = title.toString(),
                    notificationID = "",
                )

                notificationFirestore.addNotification(not).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Notify", "$task")
                    } else {
                        Log.i("Notify", "$task")
                    }
                }
            }

        }
    }






}


