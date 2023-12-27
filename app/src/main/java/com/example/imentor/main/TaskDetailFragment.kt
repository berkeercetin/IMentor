package com.example.imentor.main

import GlobalService
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R
import com.example.imentor.main.adapters.SubTaskAdapter
import com.example.imentor.main.entities.Counter
import com.example.imentor.main.entities.SubTask
import com.example.imentor.main.entities.Task
import com.example.imentor.main.modals.CustomModal
import com.example.imentor.main.services.concrates.TaskManager
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class TaskDetailFragment : Fragment() {

    private val taskService = TaskManager()
    private lateinit var subTasks: List<SubTask>
    private lateinit var task: Task
    private lateinit var sensorManager: SensorManager
    private var stepCountSensor: Sensor? = null
    private var stepCount = 0

    private val stepCountListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Gerekirse doğruluk değişikliklerini ele alın
        }

        override fun onSensorChanged(event: SensorEvent) {
            // Adım sayısını güncelleyin
            stepCount = event.values[0].toInt()
            val taskDetailCounter = view!!.findViewById<MaterialTextView>(R.id.taskDetailCounter)!!
            taskDetailCounter.text = stepCount.toString()
            // Adım sayısına bağlı olarak UI'nizi güncelleyin veya başka işlemler yapın
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepCountSensor == null) {
            // Cihazın adım sayar sensörü bulunmuyorsa işlemleri burada yapabilirsiniz
            Toast.makeText(context, "Adım sayar sensörü bulunamadı", Toast.LENGTH_LONG).show()
        } else {
            // Adım sayısı güncellemeleri için bir dinleyici kaydedin
            sensorManager.registerListener(stepCountListener, stepCountSensor, SensorManager.SENSOR_DELAY_UI)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewDetail)!!
        val taskDetailName = view.findViewById<MaterialTextView>(R.id.taskDetailName)!!
        val taskDetailCounter = view.findViewById<MaterialTextView>(R.id.taskDetailCounter)!!

        val taskDetailExplanation = view.findViewById<MaterialTextView>(R.id.taskDetailExplanation)!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val complateButton  = view.findViewById<Button>(R.id.button4)!!


        val fabButton = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabedit)!!
        fabButton.setOnClickListener {
            val fragment = AddTask()
            val bundle = Bundle()
            bundle.putString("taskID", task.taskID) // Güncellenen görevin ID'sini iletiyoruz
            bundle.putString("taskName", task.name) // Güncellenen görevin adını iletiyoruz
            bundle.putString("taskExplanation", task.explantation) // Güncellenen görevin açıklamasını iletiyoruz
            fragment.arguments = bundle
            val fragmentManager: androidx.fragment.app.FragmentManager = (context as FragmentActivity).supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val addButton = view.findViewById<Button>(R.id.button3)!!
        addButton.setOnClickListener {
            addSubTask()
        }


        arguments?.let { it ->
            val taskId = it.getString("taskID")!!
            lifecycleScope.launch {
                try {

                    task = getTask(taskId)
                    subTasks = listSubTasks(GlobalService.userId, taskId)
                    if (task.type=="sağlık"){
                        complateButton.visibility = View.GONE
                    }
                    taskDetailName.text = task.name
                    taskDetailExplanation.text = task.explantation
                    val adapter = SubTaskAdapter(subTasks,requireContext())
                    recyclerView.adapter = adapter
                    Toast.makeText(context, adapter.itemCount.toString(), Toast.LENGTH_LONG).show()

                    // Both tasks are completed
                } catch (e: Exception) {
                    // Handle exceptions appropriately
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private suspend fun listSubTasks(userId: String, taskId: String): List<SubTask> {
        val result = taskService.listSubTasks(userId, taskId).await()
        return result.map {
            SubTask(it["name"] as String, it["subTaskID"] as String)
        }
    }

    private suspend fun getTask(taskId: String): Task {
        val result = taskService.getTaskById(GlobalService.userId, taskId).await()
        return result.toObject(Task::class.java)!!
    }
    private suspend fun getCounter(taskId: String,dateTime:String) {
        val result = taskService.getTaskById(GlobalService.userId, taskId).await()
        return;
    }
    private var m_Text = ""

    private fun addSubTask() {
        val customModal = context?.let { CustomModal(it) }
        // Verileri modal'a gönderme
        customModal?.setInitialValues(task.taskID!!)
        customModal?.show()
        customModal?.show()

    }


}
