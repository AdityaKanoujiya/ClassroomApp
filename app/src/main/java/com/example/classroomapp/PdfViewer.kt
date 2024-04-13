package com.example.classroomapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.classroomapp.databinding.ActivityPdfViewerBinding
import com.github.barteksc.pdfviewer.util.FitPolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class PdfViewer : AppCompatActivity() {

    lateinit var binding : ActivityPdfViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileName = intent.extras?.getString("fileName")
        val downloadUrl = intent.extras?.getString("downloadUrl")

        lifecycleScope.launch {
            try {
                val inputStream = withContext(Dispatchers.IO) {
                    URL(downloadUrl).openStream()
                }
                binding.pdfView.fromStream(inputStream)
                    .onRender { pages ->
                        if (pages >= 1) {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                    .load()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@PdfViewer, "Failed to load PDF", Toast.LENGTH_SHORT).show()
            }
        }


//        binding.btnOpenPdf.setOnClickListener{
//            launchPdf.launch("application/pdf")
//
//        }

    }

    private val launchPdf =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri: Uri? = result.data?.data
                uri?.let {
                    binding.pdfView.fromUri(it)
                        .spacing(12)
                        .defaultPage(0)
                        .enableAnnotationRendering(false)
                        .enableDoubletap(true)
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .load()
                    binding.pdfView.useBestQuality(true)
                }
            }

            openPdfFile()
        }



    private fun openPdfFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        launchPdf.launch(intent)
    }
}