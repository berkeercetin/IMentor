package com.example.imentor.main.services.concrates

import android.util.Log
import android.widget.Toast
import com.example.imentor.main.entities.INotification
import com.example.imentor.main.entities.Task
import com.example.imentor.main.services.abstracts.NotificationService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class NotificationManager : NotificationService {
    private val db = FirebaseFirestore.getInstance()


    override fun addNotification (notification:INotification ): com.google.android.gms.tasks.Task<Void> {
        notification.notificationID = db.collection("notifications/").document().id
        notification.createDateTime = System.currentTimeMillis().toString()
        return db.document("notifications/" + notification.notificationID ).set(notification)
    }

    override fun listAll (): com.google.android.gms.tasks.Task<QuerySnapshot> {
        Log.i("notservice", "OK.")
        return db.collection("notifications/").get()
    }
}