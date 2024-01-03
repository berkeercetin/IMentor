package com.example.imentor.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.imentor.R
import com.example.imentor.main.entities.Task
import com.example.imentor.main.services.concrates.TaskManager
import java.text.SimpleDateFormat
import java.util.Calendar



private val taskService = TaskManager()

class AddTask : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(com.example.imentor.R.layout.fragment_add_task, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ClickableViewAccessibility", "CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val editTextTime = view.findViewById<EditText>(com.example.imentor.R.id.editTextStartTime)!!
        editTextTime.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val cal = Calendar.getInstance()
                        val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                            cal.set(Calendar.YEAR, year)
                            cal.set(Calendar.MONTH, month)
                            cal.set(Calendar.DAY_OF_MONTH, day)
                            editTextTime.setText(SimpleDateFormat("dd/MM/yyyy").format(cal.time))
                        }
                        DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }
                return true
            }
        })

        val editTextTime2 = view.findViewById<EditText>(com.example.imentor.R.id.editTextEndTime)!!
        editTextTime2.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val cal = Calendar.getInstance()
                        val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                            cal.set(Calendar.YEAR, year)
                            cal.set(Calendar.MONTH, month)
                            cal.set(Calendar.DAY_OF_MONTH, day)
                            editTextTime2.setText(SimpleDateFormat("dd/MM/yyyy").format(cal.time))
                        }
                        DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }
                return true
            }
        })

        val categorySpinner = view.findViewById<Spinner>(com.example.imentor.R.id.categorySpinner)!!
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parentView.getItemAtPosition(position) as String

                // "Sağlık" kategorisi seçildiyse ek inputları görünür yap

            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Bir şey seçilmediğinde yapılacaklar
            }
        }
        val textView18 = view.findViewById<TextView>(com.example.imentor.R.id.textView18)!!

        val buttonAddTask = view.findViewById<com.google.android.material.button.MaterialButton>(com.example.imentor.R.id.buttonAddTask)!!
        val editTextTaskName = view.findViewById<EditText>(com.example.imentor.R.id.addTaskName)!!
        val editTextTaskDescription = view.findViewById<EditText>(com.example.imentor.R.id.editTextTaskDescription)!!
        val editTextTaskStartTime = view.findViewById<EditText>(com.example.imentor.R.id.editTextStartTime)!!
        val editTextTaskEndTime = view.findViewById<EditText>(com.example.imentor.R.id.editTextEndTime)!!
        var taskId:String = ""
        var task = Task()
        arguments?.let {
            task= it.getSerializable("task") as Task
            editTextTaskName.setText(task.name)
            editTextTaskDescription.setText(task.explanation)
            editTextTaskStartTime.setText(task.startDateTime)
            editTextTaskEndTime.setText(task.endDateTime)
            taskId = task.taskID.toString()
            textView18.text = "Görevi Güncelle"
            buttonAddTask.text = "Görevi Güncelle"
        }

        buttonAddTask.setOnClickListener {

             if (buttonAddTask.text == "Görevi Güncelle"){
                val task = Task(
                    editTextTaskName.text.toString(),
                    taskId,
                    editTextTaskDescription.text.toString(),
                    editTextTaskStartTime.text.toString(),
                    editTextTaskEndTime.text.toString(),
                    categorySpinner.selectedItem.toString(),

                )
                taskService.updateTask(GlobalService.userId, task).addOnSuccessListener {
                    Toast.makeText(context, "Task updated successfully", Toast.LENGTH_LONG).show()
                    val fragment = HomeFragment()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }.addOnFailureListener {
                    Toast.makeText(context,  it.message.toString()+"Task could not be updated", Toast.LENGTH_LONG).show()
                }
            }
            else if (buttonAddTask.text == "Ekle"){
                Toast.makeText(context, "Task added started", Toast.LENGTH_LONG).show()
                 val task = Task(
                     editTextTaskName.text.toString(),
                     taskId,
                     editTextTaskDescription.text.toString(),
                     editTextTaskStartTime.text.toString(),
                     editTextTaskEndTime.text.toString(),
                     categorySpinner.selectedItem.toString(),
                     )
                uploadTask(task)
            }
        }
    }
    private fun uploadTask(task:Task) {
        taskService.addTask(GlobalService.userId, task).addOnSuccessListener {
            Toast.makeText(context, "Task added successfully", Toast.LENGTH_LONG).show()
            val fragment = HomeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }.addOnFailureListener {
            Toast.makeText(context,  it.message.toString()+"Task could not be added", Toast.LENGTH_LONG).show()
        }
    }

}