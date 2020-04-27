package com.greenears.tasktracker.Views.Adapters.RecyclerView

import android.os.Build
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.greenears.tasktracker.Helpers.TimeTextFormatter
import com.greenears.tasktracker.Models.PlannedTask
import com.greenears.tasktracker.Models.ViewModels.TimeTravelViewModel
import com.greenears.tasktracker.R
import io.realm.OrderedRealmCollection
import io.realm.RealmList
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import kotlinx.android.synthetic.main.item_planned_task.view.*


/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class TaskAdapter(realmResults: RealmResults<PlannedTask>) :
        RealmRecyclerViewAdapter<PlannedTask, TaskAdapter.TaskViewHolder?>(realmResults, true) {
    init {
        realmResults.addChangeListener { newResults ->
            updateData(newResults)
            reloadItems()
        }

        setHasStableIds(false)
    }

    fun reloadItems() {
        _sortedItems = null
        notifyDataSetChanged()
    }

    private var _sortedItems: List<PlannedTask>? = null
    private var _currentTimeDelta: Long = 0L

    fun setTimeDelta(timeDelta: Long) {
        _currentTimeDelta = timeDelta
        reloadItems()
    }

    private fun getSortedItems(): List<PlannedTask> {
        if (_sortedItems == null) {
            _sortedItems = data!!.sortedDescending()
        }

        return _sortedItems!!
    }

    override fun getItem(index: Int): PlannedTask? {
        return getSortedItems().get(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_planned_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.populateViews(getItem(position)!!)
    }

    inner class TaskViewHolder(itemView: View) : ViewHolder(itemView) {
        private val lowPriorityBackgroundView: View = itemView.view_background_low_priority
        private val mediumPriorityBackgroundView: View = itemView.view_background_medium_priority
        private val highPriorityBackgroundView: View = itemView.view_background_high_priority
        private val titleTextView: TextView = itemView.textview_title
        private val frequencyTextView: TextView = itemView.textview_frequency
        private val lastCompletedTextView: TextView = itemView.textview_last_completed
        private var _task: PlannedTask? = null

        fun populateViews(task: PlannedTask) {
            _task = task

            lowPriorityBackgroundView.alpha = 0f
            mediumPriorityBackgroundView.alpha = 0f
            highPriorityBackgroundView.alpha = 0f
            val priorityBackgroundView = when (_task!!.priority) {
                1 -> lowPriorityBackgroundView
                2 -> mediumPriorityBackgroundView
                3 -> highPriorityBackgroundView
                else -> null
            }

            titleTextView.text = _task!!.title

            frequencyTextView.text = when (_task!!.frequency) {
                TimeTravelViewModel.HOUR_MILLISECONDS -> "Due Hourly"
                TimeTravelViewModel.DAY_MILLISECONDS -> "Due Daily"
                TimeTravelViewModel.WEEK_MILLISECONDS -> "Due Weekly"
                else -> ""
            }

            val lastCompletion = _task!!.lastCompletionTimestamp
            val currentTime = System.currentTimeMillis() + _currentTimeDelta
            when {
                 lastCompletion == 0L -> {
                    lastCompletedTextView.setText(R.string.not_completed_yet)
                }
                lastCompletion > currentTime -> {
                    val timeUntilCompletion = lastCompletion - currentTime
                    lastCompletedTextView.setText(itemView.context.getString(
                            R.string.format_last_completed_future,
                            TimeTextFormatter.timeTextForMilliseconds(timeUntilCompletion)))
                }
                lastCompletion < currentTime -> {
                    val timeSinceCompletion = currentTime - lastCompletion
                    val timeUntilDue = _task!!.frequency - timeSinceCompletion
                    if (timeUntilDue > 0) {
                        lastCompletedTextView.setText(itemView.context.getString(
                                R.string.format_last_completed_past,
                                TimeTextFormatter.timeTextForMilliseconds(timeSinceCompletion),
                                TimeTextFormatter.timeTextForMilliseconds(timeUntilDue)))
                        priorityBackgroundView?.alpha = 0.5f *
                                (timeSinceCompletion.toFloat() / _task!!.frequency.toFloat())
                    } else {
                        lastCompletedTextView.setText(itemView.context.getString(
                                R.string.format_last_completed_past_overdue,
                                TimeTextFormatter.timeTextForMilliseconds(timeSinceCompletion),
                                TimeTextFormatter.timeTextForMilliseconds(Math.abs(timeUntilDue))))
                        priorityBackgroundView?.alpha = 0.5f
                    }
                }
            }
        }
    }
}