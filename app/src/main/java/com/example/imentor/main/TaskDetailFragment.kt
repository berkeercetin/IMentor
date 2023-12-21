package com.example.imentor.main

import GlobalService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R
import com.example.imentor.main.adapters.SubTaskAdapter
import com.example.imentor.main.entities.SubTask
import com.example.imentor.main.entities.Task
import com.example.imentor.main.modals.CustomModal
import com.example.imentor.main.services.concrates.TaskManager
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val fabButton = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabedit)!!
        fabButton.setOnClickListener {
            val fragment = AddTask()
            val bundle = Bundle()
            bundle.putString("taskID", task.taskID) // Güncellenen görevin ID'sini iletiyoruz
            bundle.putString("taskName", task.name) // Güncellenen görevin adını iletiyoruz
            bundle.putString("taskExplanation", task.explantation) // Güncellenen görevin açıklamasını iletiyoruz
            fragment.arguments = bundle
            val fragmentManager: androidx.fragment.app.FragmentManager = (context as FragmentActivity).supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val addButton = view.findViewById<Button>(R.id.button3)!!
        addButton.setOnClickListener {
            addSubTask()
        }


        arguments?.let { it ->
            val taskId = it.getString("taskID")!!
            lifecycleScope.launch {
                try {
                    task = getTask(taskId)
                    subTasks = listSubTasks(GlobalService.userId, taskId)
                    taskDetailName.text = task.name
                    taskDetailExplanation.text = task.explantation
                    val adapter = SubTaskAdapter(subTasks,requireContext())
                    recyclerView.adapter = adapter
                    Toast.makeText(context, adapter.itemCount.toString(), Toast.LENGTH_LONG).show()

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
    private var m_Text = ""

    private fun addSubTask() {
        val customModal = context?.let { CustomModal(it) }
        // Verileri modal'a gönderme
        customModal?.setInitialValues(task.taskID!!)
        customModal?.show()
        customModal?.show()

    }


}
