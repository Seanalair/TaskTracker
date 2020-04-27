package com.greenears.tasktracker.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.greenears.tasktracker.Controllers.DataStoreController
import com.greenears.tasktracker.Models.ViewModels.TimeTravelViewModel
import com.greenears.tasktracker.R
import com.greenears.tasktracker.Views.Adapters.RecyclerView.TaskHistoryAdapter
import kotlinx.android.synthetic.main.fragment_task_history.*
import kotlinx.android.synthetic.main.fragment_task_history.view.*
import kotlinx.android.synthetic.main.fragment_task_history.view.recyclerview_history

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class TaskHistoryFragment: Fragment() {
    companion object {
        fun createInstance() = TaskHistoryFragment()
    }

    private val _timeTravelViewModel: TimeTravelViewModel by activityViewModels()

    private var _historyAdapter: TaskHistoryAdapter? = null
    private val _dataStoreController = DataStoreController()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(
                R.layout.fragment_task_history,
                container,
                false)
    }

    override fun onStart() {
        super.onStart()

        _timeTravelViewModel.getTimeDelta().observe(viewLifecycleOwner, Observer<Long> { milliseconds ->
            _historyAdapter?.setTimeDelta(milliseconds)
            _dataStoreController.timeDelta = milliseconds
        })

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val taskHistory = _dataStoreController.getTaskCompletionHistory()
        _historyAdapter = TaskHistoryAdapter(taskHistory);

        recyclerview_history.setLayoutManager(LinearLayoutManager(context))
        recyclerview_history.setAdapter(_historyAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()

        _dataStoreController.close()
    }
}