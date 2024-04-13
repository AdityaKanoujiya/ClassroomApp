package com.example.classroomapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.classroomapp.databinding.ActivityPdfBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class PdfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfBinding
    private var pdfFileUri: Uri? = null
    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var classId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve class ID from intent extras
        classId = intent.getStringExtra("classId") ?: ""

        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().reference.child("pdfs")

        // Initialize Firebase Database reference for PDFs under the specific class ID
        databaseReference = FirebaseDatabase.getInstance().getReference("pdfs").child(classId)

        // Set up button click listeners
        initClickListeners()
    }

    private fun initClickListeners() {
        // Button to select a PDF file
        binding.btnSelectPdf.setOnClickListener {
            // Launch activity to select PDF files
            pdfPickerLauncher.launch("application/pdf")
        }

        // Button to upload the selected PDF file
        binding.btnUploadPdf.setOnClickListener {
            if (pdfFileUri != null) {
                uploadPdfFileToFirebase()
            } else {
                Toast.makeText(this, "Please select a PDF first", Toast.LENGTH_SHORT).show()
            }
        }

        // Button to navigate to the list of PDFs
        binding.btnShowAllPdf.setOnClickListener {
            val intent = Intent(this, AllPdfList::class.java)
            intent.putExtra("classId", classId)
            startActivity(intent)
        }
    }

    // Launches the activity to pick a PDF file
    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Update the URI for the selected PDF file
        pdfFileUri = uri

        // Extract the file name from the URI
        pdfFileUri?.let {
            val contentResolver = contentResolver
            val fileNameCursor = contentResolver.query(it, null, null, null, null)
            fileNameCursor?.use { cursor ->
                val nameIndex = cursor.getColumnIndexOrThrow("_display_name")
                cursor.moveToFirst()
                val fileName = cursor.getString(nameIndex)
                binding.pdfFileName.text = fileName
            }
        }
    }

    // Uploads the PDF file to Firebase Storage and stores its metadata in Realtime Database
    private fun uploadPdfFileToFirebase() {
        // Get the file name from the TextView
        val fileName = binding.pdfFileName.text.toString()

        // Create the storage reference with the extracted file name
        val storageRef = storageReference.child(classId).child(fileName)

        // Check if the URI is not null
        pdfFileUri?.let { uri ->
            // Upload the file to the storage reference
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    // File uploaded successfully

                    // Get the download URL of the uploaded file
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Create a PdfFile object with the file name and download URL
                        val pdfFile = PdfFile(fileName, downloadUri.toString())

                        // Add the PdfFile object to the Realtime Database
                        databaseReference.push().setValue(pdfFile)
                            .addOnSuccessListener {
                                // PDF metadata uploaded successfully
                                Toast.makeText(this, "Upload Successful", Toast.LENGTH_SHORT).show()
                                clearPdfSelection()
                            }
                            .addOnFailureListener { exception ->
                                // Handle failure in saving metadata to Realtime Database
                                Toast.makeText(this, "Failed to upload PDF metadata: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure in uploading the file
                    Toast.makeText(this, "Failed to upload PDF: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { uploadTask ->
                    // Update the progress bar based on upload progress
                    val progress = (uploadTask.bytesTransferred * 100 / uploadTask.totalByteCount).toInt()
                    binding.progressBar.progress = progress
                }
                .addOnSuccessListener {
                    // Hide the progress bar when upload is complete
                    binding.progressBar.visibility = View.GONE
                }
        }
    }

    private fun clearPdfSelection() {
        // Clear the selected PDF file and file name display
        pdfFileUri = null
        binding.pdfFileName.text = ""
        binding.progressBar.visibility = View.GONE
    }
}
