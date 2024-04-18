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
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var appDb: NotesDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val addBtn: FloatingActionButton = view.findViewById(R.id.btn_add)

        appDb = NotesDatabase.getDatabase(requireContext())

        // Initialize NotesAdapter with note click handler
        notesAdapter = NotesAdapter(
            onNoteClick = { note ->
                val bottomSheet = AddNoteFragment.newInstance(note) { updatedNote ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        appDb.notesDao().upsertNotes(updatedNote)
                    }
                }
                bottomSheet.show(requireActivity().supportFragmentManager, "AddNoteFragment")
            },
            onDeleteClick = { note ->
                lifecycleScope.launch(Dispatchers.IO) {
                    appDb.notesDao().deleteNotes(note)
                }
            },
            lifecycleOwner = viewLifecycleOwner
        )

        recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
        }

        addBtn.setOnClickListener {
            val bottomSheet = AddNoteFragment.newInstance(null) { newNote ->
                lifecycleScope.launch(Dispatchers.IO) {
                    appDb.notesDao().upsertNotes(newNote)
                }
            }
            bottomSheet.show(requireActivity().supportFragmentManager, "AddNoteFragment")
        }

        appDb.notesDao().getall().observe(viewLifecycleOwner, Observer { notesList ->
            notesAdapter.submitList(notesList)
        })

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
