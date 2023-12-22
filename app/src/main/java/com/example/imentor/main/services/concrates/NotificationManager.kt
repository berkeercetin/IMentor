package com.example.imentor.main.services.concrates

import com.example.imentor.main.entities.Task
import com.google.firebase.firestore.FirebaseFirestore

class NotificationManager {
    private val db = FirebaseFirestore.getInstance()


    fun addNotification (uid: String, task: Task): com.google.android.gms.tasks.Task<Void> {
        task.taskID = db.collection("users/$uid/tasks").document().id
        return db.document("users/$uid/tasks/" + task.taskID).set(task)
    }
}