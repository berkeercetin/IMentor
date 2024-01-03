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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
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
    private val globalService = GlobalService()
    private lateinit var tasks:List<Task>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireActivity() as MainActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.visibility = View.VISIBLE
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    @SuppressLint("Range", "SuspiciousIndentation", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val welcome = view.findViewById<TextView>(R.id.dayTextView)!!

           val recyclerView = view.findViewById<RecyclerView>(R.id.homeLoopRecyclerView)!!
            val fabButton = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAdd)!!
            val categorySpinner = view.findViewById<Spinner>(R.id.categorySpinner)!!
        val emptyStateTextView = view.findViewById<TextView>(R.id.emptyStateTextView)
           val categories = listOf("hepsi", "iş", "alışveriş","spor", "eğlence", "diğer","adım")
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
                welcome.text = "Hoşgeldin, ${GlobalService.user?.name}"
                tasks = listTasks(GlobalService.userId)
                val adapter = TaskAdapter(tasks,requireContext())
                recyclerView.adapter = adapter
                // Both tasks are completed
                if(adapter.itemCount == 0){
                    emptyStateTextView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
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