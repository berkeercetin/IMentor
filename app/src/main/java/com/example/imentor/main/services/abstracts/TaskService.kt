package com.example.imentor.main.services.abstracts



import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.example.imentor.main.entities.Task as MyTask
import com.example.imentor.main.entities.SubTask

interface TaskService {

    fun listAllTasksByUser(uid: String): Task<QuerySnapshot>

    fun getTaskById(uid: String, taskID: String): Task<DocumentSnapshot>

    fun getSubTaskById(uid: String, taskID: String, subTaskID: String): Task<DocumentSnapshot>

    fun listSubTasks(uid: String, taskID: String): Task<QuerySnapshot>

    fun addTask(uid: String, task: MyTask): Task<Void>

    fun updateTask(uid: String, task: MyTask): Task<Void>

    fun deleteTask(uid: String, taskID: String): Task<Void>

    fun addSubTask(uid: String, taskID: String, subTask: SubTask): Task<Void>

    fun updateSubTask(uid: String, taskID: String, subTask: MyTask): Task<Void>

    fun deleteSubTask(uid: String, taskID: String, subTaskID: String): Task<Void>
}
