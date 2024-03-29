package com.example.imentor.main.services.concrates

import com.example.imentor.main.entities.SubTask
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.example.imentor.main.entities.Task as MyTask

class TaskManager: com.example.imentor.main.services.abstracts.TaskService {
    private val db = FirebaseFirestore.getInstance()

    override fun listAllTasksByUser (uid: String): Task<QuerySnapshot> {
        return db.collection("users/$uid/tasks").get()
    }

    override fun getTaskById (uid:String, taskID: String): Task<DocumentSnapshot> {
        return db.document("users/$uid/tasks/$taskID").get()
    }

    override fun getSubTaskById (uid:String, taskID: String, subTaskID:String): Task<DocumentSnapshot> {
        return db.document("users/$uid/tasks/$taskID/subTasks/$subTaskID").get()
    }

    override fun listSubTasks (uid:String, taskID: String ): Task<QuerySnapshot> {
        return db.collection("users/$uid/tasks/$taskID/subTasks").get()
    }

    override fun addTask (uid: String, task: MyTask): Task<Void> {
        task.taskID = db.collection("users/$uid/tasks").document().id
        task.complated = false
        return db.document("users/$uid/tasks/" + task.taskID).set(task)
    }

    override fun updateTask (uid: String, task: MyTask): Task<Void> {
        return db.document("users/$uid/tasks/" + task.taskID).update("taskID", task.taskID,"name", task.name, "explanation", task.explanation, "startDateTime", task.startDateTime, "endDateTime", task.endDateTime,  "type", task.type, "complated", task.complated)
    }

    override fun deleteTask (uid: String, taskID: String): Task<Void> {
        return db.document("users/$uid/tasks/$taskID").delete()
    }

    override fun addSubTask (uid: String, taskID: String, subTask: SubTask): Task<Void> {
        subTask.subTaskID = db.collection("users/$uid/tasks/$taskID/subTasks").document().id
        return db.document("users/$uid/tasks/$taskID/subTasks/" + subTask.subTaskID).set(subTask)
    }

    override fun updateSubTask (uid: String, taskID: String, subTask: MyTask): Task<Void> {
        return db.document("users/$uid/tasks/$taskID/subTasks/" + subTask.taskID).set(subTask)
    }

    override fun deleteSubTask (uid: String, taskID: String, subTaskID: String): Task<Void> {
        return db.document("users/$uid/tasks/$taskID/subTasks/$subTaskID").delete()
    }

}