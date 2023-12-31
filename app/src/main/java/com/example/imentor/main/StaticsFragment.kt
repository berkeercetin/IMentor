package com.example.imentor.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.imentor.R
import com.example.imentor.main.adapters.TaskAdapter
import com.example.imentor.main.entities.Task

import com.example.imentor.main.services.concrates.TaskManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StaticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StaticsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val taskService = TaskManager()
    private lateinit var tasks:List<Task>
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
        return inflater.inflate(com.example.imentor.R.layout.fragment_statics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            try {
                tasks = listTasks(GlobalService.userId)
                val complatedTasks = tasks.filter { it.complated!! }.size
                val unComplatedTasks = tasks.size - complatedTasks
                val ratio = complatedTasks.toFloat() / tasks.size.toFloat() * 100
                val ratio2 = unComplatedTasks.toFloat() / tasks.size.toFloat() * 100
                val pieChart = view.findViewById<PieChart>(com.example.imentor.R.id.pieChart)
                val entries = ArrayList<PieEntry>()
                entries.add(PieEntry(ratio, "Tamamlandı"))
                entries.add(PieEntry(ratio2, "Tamamlanmadı"))
                val dataSet = PieDataSet(entries, "Veri Seti")
                dataSet.setColors(Color.rgb(0, 255, 0), Color.rgb(255, 0, 0))
                // Grafiği oluşturup veri setini atıyoruz
                val data = PieData(dataSet)
                pieChart.data = data

                // Grafiği güncelliyoruz
                pieChart.invalidate()



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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StaticsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StaticsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}