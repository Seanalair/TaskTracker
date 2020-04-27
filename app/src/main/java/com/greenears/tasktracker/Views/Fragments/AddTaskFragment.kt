package com.greenears.tasktracker.Views.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import com.greenears.tasktracker.Controllers.DataStoreController
import com.greenears.tasktracker.Models.ViewModels.TimeTravelViewModel
import com.greenears.tasktracker.Models.ViewModels.TimeTravelViewModel.Companion.DAY_MILLISECONDS
import com.greenears.tasktracker.Models.ViewModels.TimeTravelViewModel.Companion.HOUR_MILLISECONDS
import com.greenears.tasktracker.Models.ViewModels.TimeTravelViewModel.Companion.WEEK_MILLISECONDS
import com.greenears.tasktracker.R
import kotlinx.android.synthetic.main.fragment_add_task.*

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class AddTaskFragment: Fragment() {
    companion object {
        fun createInstance() = AddTaskFragment()
    }


    var addTaskNavigationListener: AddTaskNavigationListener? = null

    private var _chosenFrequency: Long? = null
    private var _chosenGracePeriodTier: Int? = null
    private var _chosenPriority: Int? = null

    private val _textWatcher = object: TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            updateSaveButton()
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(
                R.layout.fragment_add_task,
                container,
                false)
    }

    override fun onStart() {
        super.onStart()

        addTextWatcher()
        addClickListeners()
    }

    override fun onStop() {
        super.onStop()

        removeTextWatcher()
        removeClickListeners()
    }

    fun clearInput() {
        edittext_title?.setText("")

        fadeView(button_hourly, 0.5f)
        fadeView(button_daily, 0.5f)
        fadeView(button_weekly, 0.5f)
        fadeView(button_large_grace_period, 0.5f)
        fadeView(button_medium_grace_period, 0.5f)
        fadeView(button_small_grace_period, 0.5f)
        fadeView(button_no_grace_period, 0.5f)
        fadeView(button_low_priority, 0.5f)
        fadeView(button_medium_priority, 0.5f)
        fadeView(button_high_priority, 0.5f)
        fadeView(view_background_low_priority, 0f)
        fadeView(view_background_medium_priority, 0f)
        fadeView(view_background_high_priority, 0f)

        _chosenFrequency = null
        _chosenGracePeriodTier = null
        _chosenPriority = null
        updateSaveButton()
    }

    private fun finalizeTask() {
        if (_chosenFrequency == null ||
                _chosenGracePeriodTier == null ||
                _chosenPriority == null) {
            return
        }

        val dataStoreController = DataStoreController()

        val calculatedGracePeriod = _chosenFrequency!! * _chosenGracePeriodTier!! * 0.25

        dataStoreController.createTask(
                title = edittext_title.text.toString(),
                frequency = _chosenFrequency!!,
                leadTime = 0,
                gracePeriod = calculatedGracePeriod.toLong(),
                priority = _chosenPriority!!)

        addTaskNavigationListener?.onAddTask()

        dataStoreController.close()
    }

    private fun updateSaveButton() {
        button_save?.isEnabled = (edittext_title?.text?.isNotBlank() ?: false &&
                _chosenFrequency != null &&
                _chosenGracePeriodTier != null &&
                _chosenPriority != null)
    }

    private fun fadeView(view: View?, alpha: Float) {
        view?.takeUnless { it.alpha == alpha }?.apply {
            animate().alpha(alpha)
                    .setDuration(500)
                    .start()
        }
    }

    private fun addTextWatcher() {
        edittext_title?.addTextChangedListener(_textWatcher)
    }

    private fun removeTextWatcher() {
        edittext_title?.removeTextChangedListener(_textWatcher)
    }

    private fun addClickListeners() {
        button_hourly.setOnClickListener {
            _chosenFrequency = HOUR_MILLISECONDS

            fadeView(button_hourly, 1f)
            fadeView(button_daily, 0.5f)
            fadeView(button_weekly, 0.5f)
            updateSaveButton()
        }
        button_daily.setOnClickListener {
            _chosenFrequency = DAY_MILLISECONDS

            fadeView(button_daily, 1f)
            fadeView(button_hourly, 0.5f)
            fadeView(button_weekly, 0.5f)
            updateSaveButton()
        }
        button_weekly.setOnClickListener {
            _chosenFrequency = WEEK_MILLISECONDS

            fadeView(button_hourly, 1f)
            fadeView(button_daily, 0.5f)
            fadeView(button_weekly, 0.5f)
            updateSaveButton()
        }
        button_large_grace_period.setOnClickListener {
            _chosenGracePeriodTier = 3

            fadeView(button_large_grace_period, 1f)
            fadeView(button_medium_grace_period, 0.5f)
            fadeView(button_small_grace_period, 0.5f)
            fadeView(button_no_grace_period, 0.5f)
            updateSaveButton()
        }
        button_medium_grace_period.setOnClickListener {
            _chosenGracePeriodTier = 2

            fadeView(button_medium_grace_period, 1f)
            fadeView(button_large_grace_period, 0.5f)
            fadeView(button_small_grace_period, 0.5f)
            fadeView(button_no_grace_period, 0.5f)
            updateSaveButton()
        }
        button_small_grace_period.setOnClickListener {
            _chosenGracePeriodTier = 1

            fadeView(button_small_grace_period, 1f)
            fadeView(button_large_grace_period, 0.5f)
            fadeView(button_medium_grace_period, 0.5f)
            fadeView(button_no_grace_period, 0.5f)
            updateSaveButton()
        }
        button_no_grace_period.setOnClickListener {
            _chosenGracePeriodTier = 0

            fadeView(button_no_grace_period, 1f)
            fadeView(button_large_grace_period, 0.5f)
            fadeView(button_medium_grace_period, 0.5f)
            fadeView(button_small_grace_period, 0.5f)
            updateSaveButton()
        }
        button_low_priority.setOnClickListener {
            _chosenPriority = 1

            fadeView(view_background_low_priority, 0.25f)
            fadeView(view_background_medium_priority, 0f)
            fadeView(view_background_high_priority, 0f)
            fadeView(button_low_priority, 1f)
            fadeView(button_medium_priority, 0.5f)
            fadeView(button_high_priority, 0.5f)
            updateSaveButton()
        }
        button_medium_priority.setOnClickListener {
            _chosenPriority = 2

            fadeView(view_background_low_priority, 0f)
            fadeView(view_background_medium_priority, 0.25f)
            fadeView(view_background_high_priority, 0f)
            fadeView(button_medium_priority, 1f)
            fadeView(button_low_priority, 0.5f)
            fadeView(button_high_priority, 0.5f)
            updateSaveButton()
        }
        button_high_priority.setOnClickListener {
            _chosenPriority = 3

            fadeView(view_background_low_priority, 0f)
            fadeView(view_background_medium_priority, 0f)
            fadeView(view_background_high_priority, 0.25f)
            fadeView(button_high_priority, 1f)
            fadeView(button_low_priority, 0.5f)
            fadeView(button_medium_priority, 0.5f)
            updateSaveButton()
        }
        button_cancel.setOnClickListener {
            addTaskNavigationListener?.onCancelAddTask()
            clearInput()
        }
        button_save.setOnClickListener {
            finalizeTask()
            clearInput()
        }
    }

    private fun removeClickListeners() {
        button_hourly.setOnClickListener(null)
        button_daily.setOnClickListener(null)
        button_weekly.setOnClickListener(null)
        button_large_grace_period.setOnClickListener(null)
        button_medium_grace_period.setOnClickListener(null)
        button_small_grace_period.setOnClickListener(null)
        button_no_grace_period.setOnClickListener(null)
        button_low_priority.setOnClickListener(null)
        button_medium_priority.setOnClickListener(null)
        button_high_priority.setOnClickListener(null)
        button_cancel.setOnClickListener(null)
        button_save.setOnClickListener(null)
    }

    interface AddTaskNavigationListener {
        fun onAddTask()
        fun onCancelAddTask()
    }
}