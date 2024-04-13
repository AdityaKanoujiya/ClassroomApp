package com.example.classroomapp

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class TodoSwipeGesture(
    private val context: Context,
    private val todoAdapter: TodoAdapter,
    private val lifecycleOwner: LifecycleOwner
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val todo = todoAdapter.getTodoAtPosition(position)

        if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
            // Handle left swipe (delete)
            lifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    TodoDatabase.getDatabase(context).todoDao().deleteNotes(todo)
                }
                // Notify the adapter that an item was removed
                todoAdapter.notifyItemRemoved(position)
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Move is not implemented, so return false
        return false
    }
}


abstract class SwipeGesture(context: Context) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    val deleteColor = ContextCompat.getColor(context, R.color.delete_color)
    val deleteIcon = R.drawable.delete

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Get the position of the swiped item
        val position = viewHolder.adapterPosition

        // Handle left swipe (delete) action
        if (direction == ItemTouchHelper.LEFT) {
            // Call a function to handle the deletion of the task
            onTaskSwipedLeft(position)
        }
        // Handle other swipe directions if needed
    }

    abstract fun onTaskSwipedLeft(position: Int)
}
