package com.greenears.tasktracker.Helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.ItemTouchHelper
import com.greenears.tasktracker.R

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class SwipeToCompleteCallback(context: Context, val swipeHandler: ItemSwipeHandler):
        ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val backgroundColorAdd = ColorDrawable(ContextCompat.getColor(context, R.color.color_add))
    private val backgroundColorDelete = ColorDrawable(ContextCompat.getColor(context, R.color.color_high_priority))

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder) = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeHandler.onItemSwiped(viewHolder.adapterPosition, direction)
    }

    override fun onChildDraw(canvas: Canvas,
                             recyclerView: RecyclerView,
                             viewHolder: ViewHolder,
                             dX: Float,
                             dY: Float,
                             actionState: Int,
                             isCurrentlyActive: Boolean) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val background = when {
            dX > 0 -> backgroundColorDelete
            dX < 0 -> backgroundColorAdd
            else -> null
        }
        val itemView = viewHolder.itemView
        if (dX > 0) { // Swiping to the right
            background?.setBounds(itemView.left,
                    itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom);
        } else if (dX < 0) {
            background?.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom)
        } else {
            background?.setBounds(0, 0, 0, 0)

        }
        background?.draw(canvas)
    }

    interface ItemSwipeHandler {
        fun onItemSwiped(position: Int, direction: Int)
    }
}