package com.example.classroomapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesAdapter(
    private val onDeleteClick: suspend (NotesRecord) -> Unit,
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<NotesRecord, NotesAdapter.NotesViewHolder>(NotesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_card, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val bodyTextView: TextView = itemView.findViewById(R.id.body)
        private val dateTextView: TextView = itemView.findViewById(R.id.date)
        private val card: LinearLayout = itemView.findViewById(R.id.card)
        private val deleteButton: ImageView = itemView.findViewById(R.id.deletebtn)

        fun bind(note: NotesRecord) {
            titleTextView.text = note.title
            bodyTextView.text = note.body
            dateTextView.text = "${note.date} ${note.time}"
            card.setBackgroundColor(Color.parseColor(note.bgcolor))

            // Set OnClickListener on the delete button
            deleteButton.setOnClickListener {
                // Launch a coroutine within the lifecycle owner
                lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    onDeleteClick(note) // Call the provided suspend function to handle deletion
                }
            }
        }
    }

    private class NotesDiffCallback : DiffUtil.ItemCallback<NotesRecord>() {
        override fun areItemsTheSame(oldItem: NotesRecord, newItem: NotesRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotesRecord, newItem: NotesRecord): Boolean {
            return oldItem == newItem
        }
    }
}
