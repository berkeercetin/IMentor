package com.example.imentor.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.imentor.R
import com.example.imentor.main.entities.Task
import com.example.imentor.main.services.concrates.TaskManager
import java.text.SimpleDateFormat
import java.util.Calendar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val taskService = TaskManager()

class AddTask : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            val taskId = it.getString("taskID")!!
            val taskName = it.getString("taskName")!!
            val taskExplanation = it.getString("taskExplanation")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(com.example.imentor.R.layout.fragment_add_task, container, false)
    }

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
                        DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }
                return true
            }
        })

        val categorySpinner = view.findViewById<Spinner>(com.example.imentor.R.id.categorySpinner)!!
        val extraInputsLayout = view.findViewById<LinearLayout>(com.example.imentor.R.id.extraInputsLayout)!!
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parentView.getItemAtPosition(position) as String

                // "Sağlık" kategorisi seçildiyse ek inputları görünür yap
                if ("Sağlık" == selectedCategory) {
                    extraInputsLayout.visibility = View.VISIBLE
                } else {
                    extraInputsLayout.visibility = View.GONE
                }
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
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            taskId = it.getString("taskID")!!
            val taskName = it.getString("taskName")!!
            val taskExplanation = it.getString("taskExplanation")!!
            editTextTaskName.setText(taskName)
            editTextTaskDescription.setText(taskExplanation)
            textView18.setText("Update Task")
            buttonAddTask.setText("Update Task")
        }

        buttonAddTask.setOnClickListener {
            if(editTextTaskName.text.toString().isEmpty() || editTextTaskDescription.text.toString().isEmpty()){
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if (buttonAddTask.text == "Update Task"){
                val task = Task(
                    editTextTaskName.text.toString(),
                    taskId,
                    editTextTaskDescription.text.toString(),
                    editTextTaskStartTime.text.toString(),
                    editTextTaskEndTime.text.toString(),
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
            else if (buttonAddTask.text == "Add Task"){
                val task = Task(
                    editTextTaskName.text.toString(),
                    "",
                    editTextTaskDescription.text.toString(),
                    editTextTaskStartTime.text.toString(),
                    editTextTaskEndTime.text.toString(),
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