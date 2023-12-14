package com.example.imentor.main

import GlobalService
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R
import com.example.imentor.main.adapters.TaskAdapter
import com.example.imentor.main.services.concrates.TaskManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val taskService = TaskManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val data = arguments?.getString("taskID")
        Toast.makeText(context, "Detail: $data", Toast.LENGTH_LONG).show()
        try {
                taskService.getTaskById(GlobalService.userId, data!!).addOnSuccessListener {
                result ->
                Toast.makeText(context, "Detail: ${result.data}", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Detail: ${e.message}", Toast.LENGTH_LONG).show()
        }

        return inflater.inflate(R.layout.fragment_task_detail, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewDetail)!!
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