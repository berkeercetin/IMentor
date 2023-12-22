package com.example.imentor.main.services.concrates

import com.example.imentor.main.entities.INotification
import com.example.imentor.main.entities.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class NotificationManager {
    private val db = FirebaseFirestore.getInstance()


    fun addNotification (notification:INotification ): com.google.android.gms.tasks.Task<Void> {
        notification.notificationID = db.collection("notifications/").document().id
        return db.document("notifications/" + notification.notificationID ).set(notification)
    }

    fun listAll (): com.google.android.gms.tasks.Task<QuerySnapshot> {
        return db.collection("notifications/").get()
    }
}