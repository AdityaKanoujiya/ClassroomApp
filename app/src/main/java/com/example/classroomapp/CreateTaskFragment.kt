package com.example.classroomapp

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.classroomapp.databinding.FragmentCreateTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.classroomapp.TodoDatabase
import com.example.classroomapp.TodoRecords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateTaskBottomSheet(
    context: Context,
    private val onTaskCreated: () -> Unit // Callback when task is created
) : BottomSheetDialog(context) {

    private lateinit var binding: FragmentCreateTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskTitleEditText = binding.taskTitleEditText

        binding.btnSaveTask.setOnClickListener {
            // Get task title from EditText
            val taskTitle = taskTitleEditText.text.toString()

            // Validate title
            if (taskTitle.isNotEmpty()) {
                // Create TodoRecords instance
                val newTask = TodoRecords(task = taskTitle, checked = false)

                // Save task to database
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        TodoDatabase.getDatabase(context).todoDao().insertNotes(newTask)
                    }
                    // Notify the user and call the callback
                    Toast.makeText(context, "Task saved!", Toast.LENGTH_SHORT).show()
                    onTaskCreated()
                    dismiss()
                }
            } else {
                Toast.makeText(context, "Task title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
}

