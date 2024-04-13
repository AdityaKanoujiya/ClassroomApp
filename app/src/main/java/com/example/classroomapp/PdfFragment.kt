package com.example.classroomapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.classroomapp.databinding.FragmentPdfBinding

class PdfFragment : Fragment() {

    private lateinit var binding: FragmentPdfBinding // Initialize binding late
private var pdfFileUrl : Uri? =null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfBinding.inflate(inflater, container, false) // Inflate binding in onCreateView

        binding.btnSelectPdf.setOnClickListener(){


        }

        return binding.root // Return the root view from binding
    }


//    private val launcher = registerForActivityResult(ActivityResultContracts.GetContracts()){url ->
//        pdfFileUrl = url
//    }
}
