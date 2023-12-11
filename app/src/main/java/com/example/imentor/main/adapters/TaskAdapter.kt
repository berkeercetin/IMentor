package com.example.imentor.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R

class TaskAdapter(private val taskList: List<MutableMap<String, Any>?>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.taskListName)
        val explantationTextView: TextView = itemView.findViewById(R.id.taskListExplanation)
        val createDateTimeView: TextView = itemView.findViewById(R.id.taskListCreateDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        // Verileri ViewHolder bileşenlerine aktar
        holder.nameTextView.text = task!!["name"].toString()
        holder.explantationTextView.text = task!!["explanation"].toString()
        holder.createDateTimeView.text = task!!["startDateTime"].toString()
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}