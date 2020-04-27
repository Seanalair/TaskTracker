package com.greenears.tasktracker.Models.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class TimeTravelViewModel: ViewModel() {
    companion object {
        const val MINUTE_MILLISECONDS = 1000L * 60L
        const val HOUR_MILLISECONDS = MINUTE_MILLISECONDS * 60L
        const val DAY_MILLISECONDS = HOUR_MILLISECONDS * 24L
        const val WEEK_MILLISECONDS = DAY_MILLISECONDS * 7L
    }

    private val _timeDelta = MutableLiveData<Long>(0L)

    fun getTimeDelta(): LiveData<Long> {
        return _timeDelta
    }

    fun returnToPresent() {
        _timeDelta.postValue(0L)
    }

    fun travelForward(millis: Long) {
        _timeDelta.postValue(_timeDelta.value!! + millis)
    }

    fun travelBackward(millis: Long) {
        _timeDelta.postValue(_timeDelta.value!! - millis)
    }
}