package com.greenears.tasktracker.Application

import android.app.Application
import io.realm.Realm

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class TaskTrackerApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}