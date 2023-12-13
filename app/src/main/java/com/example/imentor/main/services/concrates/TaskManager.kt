package com.example.imentor.main.services.concrates

import android.widget.Toast
import com.example.imentor.entities.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.example.imentor.main.entities.Task as MyTask

class TaskManager {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun listAllTasksByUser (uid: String): Task<QuerySnapshot> {
        return db.collection("users/$uid/tasks").get()
    }

    fun getTaskById (uid:String,taskID: String): Task<DocumentSnapshot> {
        return db.document("users/$uid/tasks/$taskID").get()
    }

    fun addTask (uid: String, task: MyTask): Task<Void> {
        task.taskID = db.collection("users/$uid/tasks").document().id
        return db.document("users/$uid/tasks/" + task.taskID).set(task)
    }

}