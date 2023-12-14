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

    fun getSubTaskById (uid:String,taskID: String , subTaskID:String): Task<DocumentSnapshot> {
        return db.document("users/$uid/tasks/$taskID/subTasks/$subTaskID").get()
    }

    fun listSubTasks (uid:String,taskID: String ): Task<QuerySnapshot> {
        return db.collection("users/$uid/tasks/$taskID/subTasks").get()
    }

    fun addTask (uid: String, task: MyTask): Task<Void> {
        task.taskID = db.collection("users/$uid/tasks").document().id
        return db.document("users/$uid/tasks/" + task.taskID).set(task)
    }

    fun updateTask (uid: String, task: MyTask): Task<Void> {
        return db.document("users/$uid/tasks/" + task.taskID).set(task)
    }

    fun deleteTask (uid: String, taskID: String): Task<Void> {
        return db.document("users/$uid/tasks/$taskID").delete()
    }

    fun addSubTask (uid: String, taskID: String, subTask: MyTask): Task<Void> {
        subTask.taskID = db.collection("users/$uid/tasks/$taskID/subTasks").document().id
        return db.document("users/$uid/tasks/$taskID/subTasks/" + subTask.taskID).set(subTask)
    }

    fun updateSubTask (uid: String, taskID: String, subTask: MyTask): Task<Void> {
        return db.document("users/$uid/tasks/$taskID/subTasks/" + subTask.taskID).set(subTask)
    }

    fun deleteSubTask (uid: String, taskID: String, subTaskID: String): Task<Void> {
        return db.document("users/$uid/tasks/$taskID/subTasks/$subTaskID").delete()
    }

    fun addCounter (uid: String, taskID: String, counter: MyTask): Task<Void> {
        counter.taskID = db.collection("users/$uid/tasks/$taskID/counters").document().id
        return db.document("users/$uid/tasks/$taskID/counters/" + counter.taskID).set(counter)
    }

    fun updateCounter (uid: String, taskID: String, counter: MyTask): Task<Void> {
        return db.document("users/$uid/tasks/$taskID/counters/" + counter.taskID).set(counter)
    }

    fun deleteCounter (uid: String, taskID: String, counterID: String): Task<Void> {
        return db.document("users/$uid/tasks/$taskID/counters/$counterID").delete()
    }




}