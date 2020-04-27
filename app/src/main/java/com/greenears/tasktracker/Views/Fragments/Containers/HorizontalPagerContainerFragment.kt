package com.greenears.tasktracker.Views.Fragments.Containers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.*
import com.greenears.tasktracker.R
import com.greenears.tasktracker.Views.Adapters.ViewPager.HorizontalPagerAdapter
import com.greenears.tasktracker.Views.Adapters.ViewPager.VerticalPagerAdapter
import com.greenears.tasktracker.Views.Fragments.TaskListFragment
import kotlinx.android.synthetic.main.fragment_horizontal_pager.*

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class HorizontalPagerContainerFragment: Fragment(),
        HorizontalPagerAdapter.HorizontalNavigationHandler {
    companion object {
        fun createInstance() = HorizontalPagerContainerFragment()
    }

    private var _pagerAdapter: HorizontalPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(
                R.layout.fragment_horizontal_pager,
                container,
                false)
    }

    fun onBackPressed(): Boolean {
        if (viewpager_horizontal.currentItem != 1) {
            _pagerAdapter?.resetState()
            return true
        }

        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPagerAdapter()
        setupViewPager()
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar_app.setNavigationOnClickListener {
            navigateToPageIndex(0, true)
        }

        button_add_task.setOnClickListener {
            navigateToPageIndex(2, true)
        }
    }

    override fun onStop() {
        super.onStop()

        button_add_task.setOnClickListener(null)
    }

    /**
     * Create and configure pager adapter
     */
    private fun setupPagerAdapter() {
        _pagerAdapter = HorizontalPagerAdapter(fragmentManager!!)

        _pagerAdapter!!._navigationHandler = this
    }

    /**
     * Set up View pager with adapter
     */
    private fun setupViewPager() {
        viewpager_horizontal.setAdapter(_pagerAdapter)
        viewpager_horizontal.setDragPagingEnabled(false)
        viewpager_horizontal.setVerticalPagingEnabled(false)
        viewpager_horizontal.setCurrentItem(1, false)
        viewpager_horizontal.setViewMinimumUnitTranslation(
                R.id.fragment_task_history,
                0f)
        viewpager_horizontal.setViewMaximumUnitTranslation(
                R.id.fragment_task_history,
                0f)
        viewpager_horizontal.setViewMinimumUnitTranslation(
                R.id.fragment_task_list,
                0f)
        viewpager_horizontal.setViewMaximumUnitTranslation(
                R.id.fragment_task_list,
                0.6f)
    }

    override fun navigateToPageIndex(index: Int, shouldPageSmoothly: Boolean) {
        viewpager_horizontal?.setCurrentItem(index, shouldPageSmoothly)
    }
}