package com.example.classroomapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.classroomapp.databinding.FragmentAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNoteBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
     var bgColor = "#FFFFFF"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        val view = binding.root



        setupListeners()
        return view
    }

    private fun setupListeners() {
        val appDb = NotesDatabase.getDatabase(requireContext())

        // Set up card click listeners for changing background color
        binding.bgBlue.setOnClickListener { bgColor = updateBackgroundColor("#a6eaff")  }
        binding.bgPink.setOnClickListener { bgColor = updateBackgroundColor("#f3e09e") }
        binding.bgGreen.setOnClickListener {bgColor =  updateBackgroundColor("#cdffa6") }
        binding.bgRed.setOnClickListener {bgColor =  updateBackgroundColor("#D24545") }
        binding.bgYellow.setOnClickListener { bgColor = updateBackgroundColor("#f29de0") }
        binding.bgWhite.setOnClickListener { bgColor = updateBackgroundColor("#FFFFFF") }

        // Set up back button click listener
        binding.btnCancel.setOnClickListener {
            dismiss()
//            ChatFragment.addBtn.visibility = View.VISIBLE
        }

        // Set up update button click listener
        binding.btnUpdate.setOnClickListener {
            val title = binding.title.text.toString()
            val body = binding.body.text.toString()

            if (title.isNotBlank() && body.isNotBlank()) {
                val currentDateTime = Calendar.getInstance()
                val currentDate = SimpleDateFormat("dd MMM", Locale.getDefault()).format(currentDateTime.time)
                val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentDateTime.time)

                // Create a new NotesRecord instance with the entered data
                val notesRecord = NotesRecord(
                    title = title,
                    body = body,
                    bgcolor = bgColor,
                    date = currentDate,
                    time = currentTime
                )

                GlobalScope.launch {
                    appDb.notesDao().upsertNotes(notesRecord)
                }

                dismiss()
//                ChatFragment.addBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun updateBackgroundColor(color: String): String {
        binding.bg.setBackgroundColor(Color.parseColor(color))
//        binding.bg.currentColor = color
        return color
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
