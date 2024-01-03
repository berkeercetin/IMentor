package com.example.imentor.main

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
import com.example.imentor.main.adapters.NotificationAdapter
import com.example.imentor.main.entities.INotification
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class NotificationsFragment : Fragment() {
    private val notificationFirestore = com.example.imentor.main.services.concrates.NotificationManager()
    private lateinit var notifications:List<INotification>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewNotifications)!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            try {
                Log.i("notFragment", "STARTED")
                notifications = listNotification()
                val adapter = NotificationAdapter(notifications, requireContext())
                recyclerView.adapter = adapter

            } catch (e: Exception) {
                // Handle exceptions appropriately
                Log.e("TAG", e.message.toString())
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }

        }
    }

    private suspend fun listNotification(): List<INotification> {
        return notificationFirestore.listAll().await().documents.map {
            it.toObject(INotification::class.java)!!
        }
    }
}