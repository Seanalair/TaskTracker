package com.greenears.tasktracker.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.greenears.tasktracker.Controllers.DataStoreController
import com.greenears.tasktracker.Helpers.SwipeToCompleteCallback
import com.greenears.tasktracker.Helpers.TimeTextFormatter
import com.greenears.tasktracker.Models.ViewModels.TimeTravelViewModel
import com.greenears.tasktracker.R
import com.greenears.tasktracker.Views.Adapters.RecyclerView.TaskAdapter
import kotlinx.android.synthetic.main.fragment_task_list.*

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class TaskListFragment: Fragment(),
        SwipeToCompleteCallback.ItemSwipeHandler {
    companion object {
        fun createInstance() = TaskListFragment()
    }

    private val _timeTravelViewModel: TimeTravelViewModel by activityViewModels()

    private var _taskAdapter: TaskAdapter? = null
    private val _dataStoreController = DataStoreController()
    private var _swipeToCompleteCallback: SwipeToCompleteCallback? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(
                R.layout.fragment_task_list,
                container,
                false)
    }

    override fun onStart() {
        super.onStart()

        _timeTravelViewModel.getTimeDelta().observe(viewLifecycleOwner, Observer<Long> { milliseconds ->
            _taskAdapter?.setTimeDelta(milliseconds)
            _dataStoreController?.timeDelta = milliseconds
        })

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val dataStoreController = DataStoreController()
        val plannedTasks = dataStoreController.getUpcomingTasks()
        _taskAdapter = TaskAdapter(plannedTasks);

        recyclerview_tasks.setLayoutManager(LinearLayoutManager(context))
        recyclerview_tasks.setAdapter(_taskAdapter)

        _swipeToCompleteCallback = SwipeToCompleteCallback(requireContext(), this)
        val itemTouchHelper = ItemTouchHelper(_swipeToCompleteCallback!!)
        itemTouchHelper.attachToRecyclerView(recyclerview_tasks)
    }

    override fun onItemSwiped(position: Int, direction: Int) {
        val task = _taskAdapter!!.getItem(position)
        when (direction) {
            ItemTouchHelper.RIGHT -> _dataStoreController.deleteTask(task!!)
            ItemTouchHelper.LEFT -> _dataStoreController.markTaskComplete(task!!)
        }
        _taskAdapter!!.reloadItems()
    }

    override fun onDestroy() {
        super.onDestroy()

        _dataStoreController.close()
    }
}