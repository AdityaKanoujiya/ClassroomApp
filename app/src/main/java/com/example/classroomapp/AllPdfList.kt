package com.example.classroomapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classroomapp.databinding.ActivityAllPdfListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllPdfList : AppCompatActivity(), PdfFilesAdapter.PdfClickListener {

    private lateinit var binding: ActivityAllPdfListBinding
    private lateinit var adapter: PdfFilesAdapter
    private lateinit var databaseReference: DatabaseReference
    private var classId: String = ""
    var ADD_PDF_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllPdfListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve class ID from intent extras
        classId = intent.getStringExtra("classId") ?: ""

        // Initialize RecyclerView and its adapter
        adapter = PdfFilesAdapter(this)
        binding.pdfRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pdfRecyclerView.adapter = adapter

        // Set up Firebase database reference for PDFs under the specific class ID
        databaseReference = FirebaseDatabase.getInstance().getReference("pdfs").child(classId)

        // Fetch PDFs related to the selected class
        getPDFsForClass()

        // Set up click listener for Add PDF button
        binding.btnAddPdf.setOnClickListener {
            val intent = Intent(this, PdfActivity::class.java)
            intent.putExtra("classId", classId)
            startActivityForResult(intent, ADD_PDF_REQUEST)
        }
    }


    private fun getPDFsForClass() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pdfList = mutableListOf<PdfFile>()
                snapshot.children.forEach { pdfSnapshot ->
                    pdfSnapshot.getValue(PdfFile::class.java)?.let { pdf ->
                        pdfList.add(pdf)
                    }
                }

                if (pdfList.isEmpty()) {
                    Toast.makeText(this@AllPdfList, "No PDFs found for this class", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                } else {
                    adapter.submitList(pdfList)
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AllPdfList, "Failed to retrieve PDFs: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onPdfClicked(pdfFile: PdfFile) {
        val intent = Intent(this, PdfViewer::class.java)
        intent.putExtra("fileName", pdfFile.fileName)
        intent.putExtra("downloadUrl", pdfFile.downloadUrl)
        startActivity(intent)
    }
}
