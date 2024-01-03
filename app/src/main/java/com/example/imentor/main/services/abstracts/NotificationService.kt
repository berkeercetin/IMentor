package com.example.imentor.main.services.abstracts

import android.util.Log
import com.example.imentor.main.entities.INotification
import com.google.firebase.firestore.QuerySnapshot

interface NotificationService {

    fun addNotification (notification: INotification): com.google.android.gms.tasks.Task<Void>

    fun listAll (): com.google.android.gms.tasks.Task<QuerySnapshot>
}