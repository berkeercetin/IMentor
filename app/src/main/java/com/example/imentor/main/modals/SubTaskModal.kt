package com.example.imentor.main.modals

import GlobalService
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.example.imentor.R
import com.example.imentor.main.entities.SubTask
import com.example.imentor.main.services.concrates.TaskManager

class CustomModal(context: Context) : AlertDialog(context) {

    private var nameEditText: EditText? = null
    private var descriptionEditText: EditText? = null
    private var taskID: String? = null
    private val taskService = TaskManager()

    init {
        initViews()
    }

    @SuppressLint("MissingInflatedId")
    private fun initViews() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.subtask_add_dialog, null)
        setView(view)

        nameEditText = view.findViewById(R.id.editTextName)
        descriptionEditText = view.findViewById(R.id.editTextDescription)

        setTitle("Alt Görev Ekle")
        setButton(BUTTON_POSITIVE, "Kaydet") { _, _ ->
            val name = nameEditText?.text.toString()
            val description = descriptionEditText?.text.toString()
            val myTask = { name: String, description: String, taskID: String ->
                mapOf(
                    "name" to name,
                    "description" to description,
                    "taskID" to taskID
                )
            }
            val taskValues = SubTask(name, taskID!!,description,false)

            taskService.addSubTask(GlobalService.userId,taskID!!,taskValues)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Alt Görev başarıyla eklendi", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Alt Görev eklenirken bir hata oluştu", Toast.LENGTH_SHORT).show()
                    }
                        // Görev eklenirken bir hata oluştu
                    }
            // Burada adı ve açıklamayı kullanabilir veya işleyebilirsiniz
            // Örneğin, bir listener aracılığıyla ana aktiviteye iletilebilir.
        }

        setButton(BUTTON_NEGATIVE, "İptal") { dialog, _ -> dialog.dismiss() }
    }

    fun setInitialValues(task_ID: String) {
        taskID = task_ID
    }
}
