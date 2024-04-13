package com.example.classroomapp

import ClassAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classroomapp.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), ClassAdapter.ClassClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var classAdapter: ClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and its adapter
        classAdapter = ClassAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = classAdapter

        // Fetch classroom data from Firebase
        fetchClassroomData()

        // Set up button to navigate to CreateClassFragment
        binding.btnOpenPdf.setOnClickListener {
            val createClassFragment = CreateClassFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.Fragment_layout, createClassFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onClassClicked(classData: ClassDataClass) {
        // Navigate to AllPdfList activity and pass the class ID as an extra
        val intent = Intent(requireContext(), AllPdfList::class.java)
        intent.putExtra("classId", classData.id)
        startActivity(intent)
    }

    private fun fetchClassroomData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("classrooms")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val classroomList = mutableListOf<ClassDataClass>()

                for (childSnapshot in snapshot.children) {
                    val classId = childSnapshot.key ?: ""
                    val batch = childSnapshot.child("batch").getValue(String::class.java) ?: ""
                    val subject = childSnapshot.child("subject").getValue(String::class.java) ?: ""
                    val teacherName = childSnapshot.child("teacherName").getValue(String::class.java) ?: ""

                    // Create ClassDataClass object and add it to the list
                    classroomList.add(ClassDataClass(classId, batch, subject, teacherName))
                }

                // Update the data in the adapter
                classAdapter.updateData(classroomList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error (optional)
            }
        })
    }
}
