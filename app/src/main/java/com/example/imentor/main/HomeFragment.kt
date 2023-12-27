package com.example.imentor.main

import GlobalService
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.MainActivity
import com.example.imentor.R
import com.example.imentor.main.adapters.TaskAdapter
import com.example.imentor.main.entities.Task
import com.example.imentor.main.services.concrates.TaskManager
import com.example.imentor.services.concrates.UserManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class HomeFragment : Fragment() {
    private val userService = UserManager()
    private val taskService = TaskManager()
    private lateinit var tasks:List<Task>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = requireActivity() as MainActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.visibility = View.VISIBLE
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    @SuppressLint("Range", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val recyclerView = view.findViewById<RecyclerView>(R.id.homeLoopRecyclerView)!!
            val fabButton = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAdd)!!
            val categorySpinner = view.findViewById<Spinner>(R.id.categorySpinner)!!
            val categories = listOf("sağlık", "iş", "alışveriş")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
              adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
              categorySpinner.adapter = adapter

        fabButton.setOnClickListener {
                val fragment = AddTask()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            try {
                tasks = listTasks(GlobalService.userId)
                val adapter = TaskAdapter(tasks,requireContext())
                recyclerView.adapter = adapter
                // Both tasks are completed
                categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        val selectedCategory = categories[position]
                        // Burada seçilen kategoriye göre işlemler yapabilirsiniz
                        adapter.filterByCategory(selectedCategory)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // İstediğiniz bir işlemi burada yapabilirsiniz
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions appropriately
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    }

    private suspend fun listTasks(userId: String): List<Task> {
        return taskService.listAllTasksByUser(userId).await().documents.map {
            it.toObject(Task::class.java)!!
        }
    }
}