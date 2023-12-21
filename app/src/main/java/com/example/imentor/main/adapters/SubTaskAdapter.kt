package com.example.imentor.main.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R
import com.example.imentor.main.entities.SubTask

class SubTaskAdapter(
    private val taskList: List<SubTask>, private val context:Context
) : RecyclerView.Adapter<SubTaskAdapter.SubTaskViewHolder>() {

    inner class SubTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.taskListName)
        val explantationTextView: TextView = itemView.findViewById(R.id.taskListExplanation)
        val createDateTimeView: TextView = itemView.findViewById(R.id.taskListCreateDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskAdapter.SubTaskViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return SubTaskViewHolder(view)
    }


    override fun onBindViewHolder(holder: SubTaskAdapter.SubTaskViewHolder, position: Int) {
        val task = taskList[position]

        // Verileri ViewHolder bileşenlerine aktar
        holder.nameTextView.text = task.name
//        holder.explantationTextView.text = task.explantation
    }


    override fun getItemCount(): Int {
        return taskList.size
    }
}