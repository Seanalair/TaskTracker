package com.greenears.tasktracker.Views.Adapters.ViewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenears.tasktracker.Views.Fragments.AddTaskFragment
import com.greenears.tasktracker.Views.Fragments.Containers.HorizontalPagerContainerFragment
import com.greenears.tasktracker.Views.Fragments.SettingsFragment
import com.greenears.tasktracker.Views.Fragments.TaskListFragment
import java.lang.IllegalStateException

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class VerticalPagerAdapter(fragmentManager: FragmentManager):
        FragmentPagerAdapter(fragmentManager){

    companion object {
        val LIST_FRAGMENT_INDEX = 0
        val SETTINGS_FRAGMENT_INDEX = 1
    }

    private var _horizontalPagerFragment: HorizontalPagerContainerFragment? = null
    private var _settingsFragment: SettingsFragment? = null

    fun onBackPressed(): Boolean {
        return _horizontalPagerFragment?.onBackPressed() ?: false
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            LIST_FRAGMENT_INDEX -> {
                if (_horizontalPagerFragment == null) {
                    _horizontalPagerFragment = HorizontalPagerContainerFragment.createInstance()
                }

                _horizontalPagerFragment as Fragment
            }
            SETTINGS_FRAGMENT_INDEX -> {
                if (_settingsFragment == null) {
                    _settingsFragment = SettingsFragment.createInstance()
                }

                _settingsFragment as Fragment
            }
            else -> throw IllegalStateException("Invalid Index Accessed in View Pager Adapter")
        }
    }
}