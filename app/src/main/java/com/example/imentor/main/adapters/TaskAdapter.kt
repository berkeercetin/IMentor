package com.example.imentor.main.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R
import com.example.imentor.main.TaskDetailFragment

class TaskAdapter(
    private val taskList: List<MutableMap<String, Any>? >, private val context:Context
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

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

        holder.itemView.setOnClickListener {
            val taskID = task["taskID"].toString()
                val fragment = TaskDetailFragment()
                val bundle = Bundle()
                bundle.putString("taskID", taskID)
                Toast.makeText(context, "Adapter: $taskID", Toast.LENGTH_LONG).show()
                fragment.arguments = bundle
                val fragmentManager: androidx.fragment.app.FragmentManager = (context as FragmentActivity).supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
        }


    }


    override fun getItemCount(): Int {
        return taskList.size
    }
}