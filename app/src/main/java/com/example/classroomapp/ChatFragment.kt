//package com.example.classroomapp

package com.example.classroomapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {
    private lateinit var appDb: NotesDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        appDb = NotesDatabase.getDatabase(requireContext())
        val addBtn: FloatingActionButton = view.findViewById(R.id.btn_add)
        recyclerView = view.findViewById(R.id.recyclerView)

        // Set up RecyclerView and Adapter
        notesAdapter = NotesAdapter(
            onDeleteClick = { note ->
                // Launch a coroutine to delete the note when onDeleteClick is called
                lifecycleScope.launch(Dispatchers.IO) {
                    appDb.notesDao().deleteNotes(note)
                }
            },
            lifecycleOwner = viewLifecycleOwner // Pass the lifecycle owner to the adapter
        )

        recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
        }

        addBtn.setOnClickListener {
            val bottomSheet = AddNoteBottomSheet()
            bottomSheet.show(requireActivity().supportFragmentManager, "AddNoteBottomSheet")
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Refresh the notes list when the fragment is resumed
        refreshNotesList()
    }

    private fun refreshNotesList() {
        // Observe changes in the notes data and update the adapter
        appDb.notesDao().getall().observe(viewLifecycleOwner, Observer { notesList ->
            notesAdapter.submitList(notesList)
        })
    }
}
