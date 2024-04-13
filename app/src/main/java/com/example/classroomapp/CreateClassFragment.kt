package com.example.classroomapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.classroomapp.databinding.FragmentCreateClassBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateClassFragment : Fragment() {

    private lateinit var binding: FragmentCreateClassBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var classCounterReference: DatabaseReference
    private var newClassId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase database references
        databaseReference = FirebaseDatabase.getInstance().getReference("classrooms")
        classCounterReference = FirebaseDatabase.getInstance().getReference("counters").child("classrooms")

        // Set up button click listener
        binding.button.setOnClickListener {
            val batch = binding.txtBatch.text.toString()
            val subject = binding.txtSubject.text.toString()
            val teacherName = binding.txtTeacherName.text.toString()

            if (batch.isNotEmpty() && subject.isNotEmpty() && teacherName.isNotEmpty()) {
                fetchAndIncrementClassCounter(batch, subject, teacherName)
            } else {
                Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAndIncrementClassCounter(batch: String, subject: String, teacherName: String) {
        // Fetch the current class counter from Firebase
        classCounterReference.get().addOnSuccessListener { snapshot ->
            val currentCounter = snapshot.value as? Long ?: 0L
            // Increment the counter
            newClassId = (currentCounter + 1).toString()

            // Update the class counter in Firebase
            classCounterReference.setValue(currentCounter + 1)
                .addOnSuccessListener {
                    // Successfully updated the counter, proceed to create the classroom
                    createClassroom(newClassId!!, batch, subject, teacherName)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Failed to update class counter: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to fetch class counter", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createClassroom(classId: String, batch: String, subject: String, teacherName: String) {
        // Create a data class representing the classroom details
        val classroomData = ClassDataClass(classId, batch, subject, teacherName)

        // Upload classroom data to Firebase
        databaseReference.child(classId).setValue(classroomData)
            .addOnSuccessListener {
                // Classroom created successfully
                Toast.makeText(requireContext(), "Classroom created successfully", Toast.LENGTH_SHORT).show()

                // Return to the previous screen or handle the navigation as needed
                requireActivity().finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error creating classroom: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
