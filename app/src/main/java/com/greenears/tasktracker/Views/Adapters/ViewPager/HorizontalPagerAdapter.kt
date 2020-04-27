package com.greenears.tasktracker.Views.Adapters.ViewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenears.tasktracker.Views.Fragments.AddTaskFragment
import com.greenears.tasktracker.Views.Fragments.TaskHistoryFragment
import com.greenears.tasktracker.Views.Fragments.TaskListFragment
import java.lang.IllegalStateException

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class HorizontalPagerAdapter(fragmentManager: FragmentManager):
        FragmentPagerAdapter(fragmentManager),
        AddTaskFragment.AddTaskNavigationListener{

    companion object {
        val HISTORY_FRAGMENT_INDEX = 0
        val LIST_FRAGMENT_INDEX = 1
        val ADD_TASK_FRAGMENT_INDEX = 2
    }

    private var _taskHistoryFragment: TaskHistoryFragment? = null
    private var _taskListFragment: TaskListFragment? = null
    private var _addTaskFragment: AddTaskFragment? = null

    public var _navigationHandler: HorizontalNavigationHandler? = null

    fun resetState() {
        _addTaskFragment?.clearInput()
        _navigationHandler?.navigateToPageIndex(LIST_FRAGMENT_INDEX, true)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            HISTORY_FRAGMENT_INDEX -> {
                if (_taskHistoryFragment == null) {
                    _taskHistoryFragment = TaskHistoryFragment.createInstance()
                }

                _taskHistoryFragment as Fragment
            }
            LIST_FRAGMENT_INDEX -> {
                if (_taskListFragment == null) {
                    _taskListFragment = TaskListFragment.createInstance()
                }

                _taskListFragment as Fragment
            }
            ADD_TASK_FRAGMENT_INDEX -> {
                if (_addTaskFragment == null) {
                    _addTaskFragment = AddTaskFragment.createInstance()

                    _addTaskFragment!!.addTaskNavigationListener = this
                }

                _addTaskFragment as Fragment
            }
            else -> throw IllegalStateException("Invalid Index Accessed in View Pager Adapter")
        }
    }

    override fun onAddTask() {
        _navigationHandler?.navigateToPageIndex(LIST_FRAGMENT_INDEX, true)
    }

    override fun onCancelAddTask() {
        _navigationHandler?.navigateToPageIndex(LIST_FRAGMENT_INDEX, true)
    }

    interface HorizontalNavigationHandler {
        fun navigateToPageIndex(index: Int, shouldPageSmoothly: Boolean)
    }
}