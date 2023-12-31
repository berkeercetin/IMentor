package com.example.imentor.main.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R
import com.example.imentor.main.entities.INotification

class NotificationAdapter(
    private val notificationList: List<INotification>, private val context:Context
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.notificationListName)
        val explantationTextView: TextView = itemView.findViewById(R.id.notificationListExplanation)
        val createDateTimeView: TextView = itemView.findViewById(R.id.dateTimeNotificationList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.NotificationViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_list_item, parent, false)
        return NotificationViewHolder(view)
    }


    override fun onBindViewHolder(holder: NotificationAdapter.NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        Log.i("notAdapter", notification.toString())
        // Verileri ViewHolder bileşenlerine aktar
        holder.nameTextView.text = notification.body
        holder.explantationTextView.text = notification.title
        holder.createDateTimeView.text = notification.createDateTime.toString()
    }


    override fun getItemCount(): Int {
        return notificationList.size
    }
}