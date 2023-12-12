package com.example.imentor.main

import GlobalService
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.MainActivity
import com.example.imentor.R
import com.example.imentor.entities.User
import com.example.imentor.main.adapters.TaskAdapter
import com.example.imentor.main.services.concrates.TaskManager
import com.example.imentor.services.concrates.UserManager
import com.google.android.material.navigation.NavigationView


class HomeFragment : Fragment() {
    private val userService = UserManager()
    private val taskService = TaskManager()

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


    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val recyclerView = view.findViewById<RecyclerView>(R.id.homeLoopRecyclerView)!!
            val fabButton = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAdd)!!
            fabButton.setOnClickListener {
                val fragment = AddTask()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskService.listAllTasksByUser(GlobalService.userId).addOnSuccessListener {
            result ->
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


}