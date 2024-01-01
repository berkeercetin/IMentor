package com.example.imentor.main

import GlobalService
import android.annotation.SuppressLint

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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R
import com.example.imentor.main.adapters.SubTaskAdapter
import com.example.imentor.main.entities.SubTask
import com.example.imentor.main.entities.Task
import com.example.imentor.main.modals.CustomModal
import com.example.imentor.main.services.concrates.TaskManager
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.Serializable


class TaskDetailFragment : Fragment() {

    private val taskService = TaskManager()
    private lateinit var subTasks: List<SubTask>
    private lateinit var task: Task
    private var sensorManager: SensorManager? = null
    private var stepCountSensor: Sensor? = null
    private var stepCount = 0f

    private val stepCountListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Gerekirse doğruluk değişikliklerini ele alın
        }

        override fun onSensorChanged(event: SensorEvent) {
            // Adım sayısını güncelleyin
            stepCount = event.values[0]
            val taskDetailCounter = view!!.findViewById<MaterialTextView>(R.id.taskDetailCounter)!!
            taskDetailCounter.text = stepCount.toString()
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewDetail)!!
        val taskDetailName = view.findViewById<MaterialTextView>(R.id.taskDetailName)!!
        val taskStart = view.findViewById<TextView>(R.id.detailStart)!!
        val taskEnd = view.findViewById<TextView>(R.id.detailEnd)!!

        val taskDetailExplanation = view.findViewById<MaterialTextView>(R.id.taskDetailExplanation)!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val complateButton  = view.findViewById<Button>(R.id.complate)!!
        complateButton.setOnClickListener {
            complate()
        }
        val deleteButton  = view.findViewById<Button>(R.id.delete)!!
        deleteButton.setOnClickListener{
            delete()
        }

        val fabButton = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabedit)!!
        fabButton.setOnClickListener {
            val fragment = AddTask()
            val bundle = Bundle()
            bundle.putString("taskID", task.taskID) // Güncellenen görevin ID'sini iletiyoruz
            fragment.arguments = bundle
            bundle.putSerializable("task", task);
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
                    val textView4 = view.findViewById<TextView>(R.id.textView4)!!
                    val taskDetailCounter = view.findViewById<MaterialTextView>(R.id.taskDetailCounter)!!

                    task = getTask(taskId)
                    taskStart.text = "Başlangıç Tarihi: " + task.startDateTime
                    taskEnd.text = "Bitiş Tarihi: " + task.endDateTime
                    if(task.complated!!){
                        complateButton.visibility = View.GONE
                    }

                    subTasks = listSubTasks(GlobalService.userId, taskId)
                    if (task.type=="adım"){
                        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
                        stepCountSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

                        if (stepCountSensor == null) {
                            // Cihazın adım sayar sensörü bulunmuyorsa işlemleri burada yapabilirsiniz
                            Toast.makeText(context, "Adım sayar sensörü bulunamadı", Toast.LENGTH_LONG).show()
                        } else {
                            // Adım sayısı güncellemeleri için bir dinleyici kaydedin
                            Toast.makeText(context, "Adım sayar sensörü bulundu", Toast.LENGTH_LONG).show()
                            sensorManager?.registerListener(stepCountListener, stepCountSensor, SensorManager.SENSOR_DELAY_UI)
                        }
                        complateButton.visibility = View.GONE
                    }
                    else {
                        taskDetailCounter.visibility = View.GONE
                    }
                    taskDetailName.text = task.name
                    taskDetailExplanation.text = task.explanation
                    val adapter = SubTaskAdapter(subTasks,requireContext())
                    if (subTasks.isEmpty()) {
                        textView4.visibility = View.VISIBLE
                    }else{
                        recyclerView.adapter = adapter
                        textView4.visibility = View.GONE
                    }


                    // Both tasks are completed
                } catch (e: Exception) {
                    // Handle exceptions appropriately
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun delete() {
        taskService.deleteTask(GlobalService.userId, task.taskID!!).addOnSuccessListener {
            val fragment = HomeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }.addOnFailureListener {
            Toast.makeText(context,  it.message.toString()+"Task could not be deleted", Toast.LENGTH_LONG).show()
        }
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

    private fun complate(){
        task.complated = true
        taskService.updateTask(GlobalService.userId, task).addOnSuccessListener {
            val fragment = HomeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }.addOnFailureListener {
            Toast.makeText(context,  it.message.toString()+"Task could not be updated", Toast.LENGTH_LONG).show()
        }
    }


}
