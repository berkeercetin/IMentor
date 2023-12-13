package com.example.imentor.main.services.concrates

import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class TaskManager {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun listAllTasksByUser (uid: String): Task<QuerySnapshot> {
        return db.collection("users/$uid/tasks").get()
    }

    fun getTaskById (uid:String,taskID: String): Task<DocumentSnapshot> {
        return db.document("users/$uid/tasks/$taskID").get()
    }
}