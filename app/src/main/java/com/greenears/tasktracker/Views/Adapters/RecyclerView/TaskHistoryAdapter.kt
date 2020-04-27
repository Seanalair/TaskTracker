package com.greenears.tasktracker.Views.Adapters.RecyclerView

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.greenears.tasktracker.Helpers.TimeTextFormatter
import com.greenears.tasktracker.Models.CompletedTask
import com.greenears.tasktracker.R
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_task_history.view.*

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class TaskHistoryAdapter(data: OrderedRealmCollection<CompletedTask?>?) :
        RealmRecyclerViewAdapter<CompletedTask?, TaskHistoryAdapter.TaskHistoryViewHolder?>(data, true) {
    init {
        setHasStableIds(false)
    }

    private var _currentTimeDelta: Long = 0L

    fun setTimeDelta(timeDelta: Long) {
        _currentTimeDelta = timeDelta
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task_history, parent, false)
        return TaskHistoryViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: TaskHistoryViewHolder, position: Int) {
        holder.populateViews(getItem(position)!!)
    }

    inner class TaskHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lowPriorityBackgroundView: View = itemView.view_background_low_priority
        private val mediumPriorityBackgroundView: View = itemView.view_background_medium_priority
        private val highPriorityBackgroundView: View = itemView.view_background_high_priority
        private val titleTextView: TextView = itemView.textview_title
        private val completedTextView: TextView = itemView.textview_completed

        private var _taskHistoryEntry: CompletedTask? = null

        fun populateViews(task: CompletedTask) {
            _taskHistoryEntry = task

            lowPriorityBackgroundView.alpha = 0f
            mediumPriorityBackgroundView.alpha = 0f
            highPriorityBackgroundView.alpha = 0f
            val priorityBackgroundView = when (_taskHistoryEntry!!.task!!.priority) {
                1 -> lowPriorityBackgroundView
                2 -> mediumPriorityBackgroundView
                3 -> highPriorityBackgroundView
                else -> null
            }

            priorityBackgroundView?.alpha = 0.25f
            titleTextView.text = _taskHistoryEntry!!.task?.title

            val completion = _taskHistoryEntry!!.completionTimestamp
            val currentTime = System.currentTimeMillis() + _currentTimeDelta
            when {
                completion > currentTime -> {
                    val timeUntilCompletion = completion - currentTime
                    completedTextView.setText(itemView.context.getString(
                            R.string.completed_future,
                            TimeTextFormatter.timeTextForMilliseconds(timeUntilCompletion)))
                }
                completion < currentTime -> {
                    val timeSinceCompletion = currentTime - completion
                    completedTextView.setText(itemView.context.getString(
                            R.string.completed_past,
                            TimeTextFormatter.timeTextForMilliseconds(timeSinceCompletion)))
                }
            }
        }
    }
}