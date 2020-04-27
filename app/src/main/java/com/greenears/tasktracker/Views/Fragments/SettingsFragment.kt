package com.greenears.tasktracker.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.greenears.tasktracker.Helpers.TimeTextFormatter
import com.greenears.tasktracker.Models.ViewModels.TimeTravelViewModel
import com.greenears.tasktracker.R
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */
class SettingsFragment: Fragment() {
    companion object {
        fun createInstance() = SettingsFragment()
    }

    private val _timeTravelViewModel: TimeTravelViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(
                R.layout.fragment_settings,
                container,
                false)
    }

    override fun onStart() {
        super.onStart()

        _timeTravelViewModel.getTimeDelta().observe(viewLifecycleOwner, Observer<Long> { milliseconds ->
            if (milliseconds == 0L) {
                textview_time_traveled.setText(R.string.app_showing_present)
            } else {
                val timeText = TimeTextFormatter.timeTextForMilliseconds(Math.abs(milliseconds), false)
                val format = if (milliseconds > 0L) {
                    R.string.format_app_showing_future
                } else {
                    R.string.format_app_showing_past
                }

                textview_time_traveled.text = getString(format, timeText)
            }
        })

        addClickListeners()
    }

    override fun onStop() {
        super.onStop()

        removeClickListeners()
    }

    private fun addClickListeners() {
        button_add_week.setOnClickListener{
            _timeTravelViewModel.travelForward(TimeTravelViewModel.WEEK_MILLISECONDS)
        }
        button_subtract_week.setOnClickListener{
            _timeTravelViewModel.travelBackward(TimeTravelViewModel.WEEK_MILLISECONDS)
        }
        button_add_day.setOnClickListener{
            _timeTravelViewModel.travelForward(TimeTravelViewModel.DAY_MILLISECONDS)
        }
        button_subtract_day.setOnClickListener{
            _timeTravelViewModel.travelBackward(TimeTravelViewModel.DAY_MILLISECONDS)
        }
        button_add_hour.setOnClickListener{
            _timeTravelViewModel.travelForward(TimeTravelViewModel.HOUR_MILLISECONDS)
        }
        button_subtract_hour.setOnClickListener{
            _timeTravelViewModel.travelBackward(TimeTravelViewModel.HOUR_MILLISECONDS)
        }
        button_add_minute.setOnClickListener{
            _timeTravelViewModel.travelForward(TimeTravelViewModel.MINUTE_MILLISECONDS)
        }
        button_subtract_minute.setOnClickListener{
            _timeTravelViewModel.travelBackward(TimeTravelViewModel.MINUTE_MILLISECONDS)
        }
    }

    private fun removeClickListeners() {
        button_add_week.setOnClickListener(null)
        button_subtract_week.setOnClickListener(null)
        button_add_day.setOnClickListener(null)
        button_subtract_day.setOnClickListener(null)
        button_add_hour.setOnClickListener(null)
        button_subtract_hour.setOnClickListener(null)
        button_add_minute.setOnClickListener(null)
        button_subtract_minute.setOnClickListener(null)
    }
}