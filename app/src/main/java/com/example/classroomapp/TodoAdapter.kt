package com.example.classroomapp


import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class TodoAdapter : ListAdapter<TodoRecords, TodoAdapter.TodoViewHolder>(TodoDiffCallback())
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_design, parent, false)
        return TodoViewHolder(view)
    }

    override public fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = getItem(position)
        holder.bind(todo)
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskCheckBox: CheckBox = itemView.findViewById(R.id.task)

        fun bind(todo: TodoRecords) {
            taskCheckBox.text = todo.task
            taskCheckBox.isChecked = false

            // Apply strikethrough style based on the checked status
            if (todo.checked == true) {
                taskCheckBox.paintFlags = taskCheckBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                taskCheckBox.paintFlags = taskCheckBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // Set listener for checkbox changes
            taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                // Update the 'checked' property of the TodoRecords item
                todo.checked = isChecked
                // Update the UI to reflect the change
                if (isChecked) {
                    taskCheckBox.paintFlags = taskCheckBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    taskCheckBox.paintFlags = taskCheckBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }
    }

    private class TodoDiffCallback : DiffUtil.ItemCallback<TodoRecords>() {
        override fun areItemsTheSame(oldItem: TodoRecords, newItem: TodoRecords): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoRecords, newItem: TodoRecords): Boolean {
            return oldItem == newItem
        }
    }

    fun getTodoAtPosition(position: Int): TodoRecords {
        return getItem(position)
    }
}
