package com.example.imentor.main

import GlobalService
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R
import com.example.imentor.main.adapters.TaskAdapter
import com.example.imentor.main.entities.SubTask
import com.example.imentor.main.entities.Task
import com.example.imentor.main.services.concrates.TaskManager
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class TaskDetailFragment : Fragment() {

    private val taskService = TaskManager()
    private lateinit var subTasks: List<SubTask>
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewDetail)!!
        val taskDetailName = view.findViewById<MaterialTextView>(R.id.taskDetailName)!!
        val taskDetailExplanation = view.findViewById<MaterialTextView>(R.id.taskDetailExplanation)!!
        arguments?.let {
            val taskId = it.getString("taskID")!!
            lifecycleScope.launch {
                try {
                    task = getTask(taskId)
                    subTasks = listSubTasks(GlobalService.userId, taskId)
                    Toast.makeText(context, "Both initialized", Toast.LENGTH_LONG).show()
                    taskDetailName.text = task.name
                    taskDetailExplanation.text = task.explantation
                    // Both tasks are completed
                } catch (e: Exception) {
                    // Handle exceptions appropriately
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private suspend fun listSubTasks(userId: String, taskId: String): List<SubTask> {
        val result = taskService.listSubTasks(userId, taskId).await()
        return result.map {
            SubTask(it["name"] as String, it["subTaskID"] as String)
        }
    }

    private suspend fun getTask(taskId: String): Task {
        val result = taskService.getTaskById(GlobalService.userId, taskId).await()
        return result.toObject(Task::class.java)!!
    }

    }
