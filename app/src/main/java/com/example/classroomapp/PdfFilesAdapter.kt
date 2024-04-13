package com.example.classroomapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.classroomapp.databinding.PdfFiledesignBinding



class PdfFilesAdapter(private val listener: PdfClickListener) : ListAdapter<PdfFile, PdfFilesAdapter.PdfFilesViewHolder>(pdfDiffCallback()) {

    inner class PdfFilesViewHolder(private val binding : PdfFiledesignBinding ): RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener(){
                listener.onPdfClicked(getItem(adapterPosition))
            }
        }

        fun bind(data : PdfFile){
            binding.pdfName.text = data.fileName
        }
    }

    class pdfDiffCallback : DiffUtil.ItemCallback<PdfFile>(){
        override fun areContentsTheSame(oldItem: PdfFile, newItem: PdfFile) = oldItem.downloadUrl == newItem.downloadUrl


        override fun areItemsTheSame(oldItem: PdfFile, newItem: PdfFile): Boolean = oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfFilesViewHolder {
        val binding = PdfFiledesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PdfFilesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PdfFilesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface PdfClickListener {
        fun onPdfClicked(pdfFile: PdfFile)
    }
}
