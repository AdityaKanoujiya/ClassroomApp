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
var bgColor = "#FFFFFF"


class AddNoteFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(note: NotesRecord? = null, saveAction: (NotesRecord) -> Unit): AddNoteFragment {
            val fragment = AddNoteFragment()
            fragment.note = note
            fragment.saveAction = saveAction
            return fragment
        }
    }

    private var note: NotesRecord? = null
    private lateinit var saveAction: (NotesRecord) -> Unit

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()

        // Pre-fill the fields if a note is provided
        note?.let {
            binding.title.setText(it.title)
            binding.body.setText(it.body)
            bgColor = it.bgcolor.toString()
            updateBackgroundColor(bgColor)
        }
    }

    private fun setupListeners() {
        // Save button click listener
        binding.btnUpdate.setOnClickListener {
            val title = binding.title.text.toString()
            val body = binding.body.text.toString()

            if (title.isNotBlank() && body.isNotBlank()) {
                val currentDateTime = Calendar.getInstance()
                val currentDate = SimpleDateFormat("dd MMM", Locale.getDefault()).format(currentDateTime.time)
                val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentDateTime.time)

                // If a note is provided, update its fields
                val notesRecord = note ?: NotesRecord(
                    title = title,
                    body = body,
                    bgcolor = bgColor,
                    date = currentDate,
                    time = currentTime
                )

                notesRecord.title = title
                notesRecord.body = body
                notesRecord.bgcolor = bgColor

                // Call saveAction to save or update the note
                saveAction(notesRecord)
                dismiss()
            }
        }

        // Set up background color changing functionality
        binding.bgBlue.setOnClickListener { updateBackgroundColor("#a6eaff") }
        binding.bgPink.setOnClickListener { updateBackgroundColor("#f29de0") }
        binding.bgGreen.setOnClickListener { updateBackgroundColor("#cdffa6") }
        binding.bgRed.setOnClickListener { updateBackgroundColor("#d24545") }
        binding.bgYellow.setOnClickListener { updateBackgroundColor("#f3e09e") }
        binding.bgWhite.setOnClickListener { updateBackgroundColor("#ffffff") }

        // Set up cancel button listener
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun updateBackgroundColor(color: String) {
        bgColor = color
        binding.bg.setBackgroundColor(Color.parseColor(color))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference
    }
}

