package com.example.classroomapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classroomapp.databinding.FragmentTodoBinding

class TodoFragment : Fragment() {
    private var binding: FragmentTodoBinding? = null
    private var appDb: TodoDatabase? = null
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentTodoBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        val view = fragmentBinding.root

        // Initialize database
        appDb = TodoDatabase.getDatabase(requireContext())

        // Initialize RecyclerView and Adapter
        todoAdapter = TodoAdapter()
        fragmentBinding.todoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todoAdapter
        }

        // Set up observer for changes in the todo list
        appDb?.todoDao()?.getall()?.observe(viewLifecycleOwner, Observer { todoList ->
            todoAdapter.submitList(todoList)
        })

        // Setup add button
        fragmentBinding.btnCreateTask.setOnClickListener {
            // Show bottom sheet dialog to create a new task
            val bottomSheet = CreateTaskBottomSheet(requireContext()) {
                // Refresh the list after a new task is created
                refreshTodoList()
            }
            bottomSheet.show()
        }

        // Initialize ItemTouchHelper and attach it to RecyclerView
        val itemTouchHelper = ItemTouchHelper(TodoSwipeGesture(requireContext(), todoAdapter, viewLifecycleOwner))
        itemTouchHelper.attachToRecyclerView(binding?.todoRecyclerView)


        return view
    }

    private fun refreshTodoList() {
        // Refresh the todo list when the fragment is resumed
        appDb?.todoDao()?.getall()?.observe(viewLifecycleOwner, Observer { todoList ->
            todoAdapter.submitList(todoList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up reference to avoid memory leaks
        binding = null
    }
}
