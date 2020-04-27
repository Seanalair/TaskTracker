package com.greenears.tasktracker.Views.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.greenears.tasktracker.R
import com.greenears.tasktracker.Views.Adapters.ViewPager.VerticalPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var _pagerAdapter: VerticalPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPagerAdapter()
        setupViewPager()
    }

    override fun onBackPressed() {
        var backActionHandled = false

        if (viewpager_vertical.currentItem != 0) {
            backActionHandled = true
            viewpager_vertical.setCurrentItem(0, true)
        }

        if (!backActionHandled) {
            backActionHandled = _pagerAdapter?.onBackPressed() ?: false
        }

        if (!backActionHandled) {
            super.onBackPressed()
        }
    }

    /**
     * Create and configure pager adapter
     */
    private fun setupPagerAdapter() {
        _pagerAdapter = VerticalPagerAdapter(supportFragmentManager)
    }

    /**
     * Set up View pager with adapter
     */
    private fun setupViewPager() {
        viewpager_vertical.setAdapter(_pagerAdapter)
        viewpager_vertical.setDragPagingEnabled(true)
        viewpager_vertical.setVerticalPagingEnabled(true)
        viewpager_vertical.setViewMinimumUnitTranslation(
                R.id.fragment_horizontal_pager_container,
                0f)
        viewpager_vertical.setViewMaximumUnitTranslation(
                R.id.fragment_horizontal_pager_container,
                0f)
        viewpager_vertical.setViewMinimumUnitTranslation(
                R.id.settings_fragment,
                0.6f)
    }
}
