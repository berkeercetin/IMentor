package com.example.imentor.main

import GlobalService
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R
import com.example.imentor.main.adapters.TaskAdapter
import com.example.imentor.main.entities.Task
import com.example.imentor.main.services.concrates.TaskManager


class TaskDetailFragment : Fragment() {

    private val taskService = TaskManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewDetail)!!
        val taskDetailName = view.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.taskDetailName)!!
        val taskDetalExplantation = view.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.taskDetailExplanation)!!
        val taskDetailCounter = view.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.taskDetailCounter)!!
        val data = arguments?.getString("taskID")
        try {
            taskService.getTaskById(GlobalService.userId, data!!).addOnSuccessListener {
                    result ->
                taskDetailName.text = result.data?.get("name").toString()
                taskDetalExplantation.text = result.data?.get("explantation").toString()
                //taskDetailCounter.text = result.data?.get("counter").toString()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Detail: ${e.message}", Toast.LENGTH_LONG).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        arguments?.getString("taskID")?.let {
            taskService.listSubTasks(GlobalService.userId, it).addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("TAG", document.data.toString())
                }
                val taskList = result.documents.map { it -> it.data }
                if (taskList.isNotEmpty()) {
                    // Veriler mevcut, RecyclerView.Adapter'e aktarın
                    val adapter = TaskAdapter(taskList,requireContext())
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
                Log.e("TAG",taskList.size.toString())
            }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "Error getting documents: ", exception)
                }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}