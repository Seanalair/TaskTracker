package com.greenears.tasktracker.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

open class CompletedTask(
        var task: PlannedTask? = null,
        var completionTimestamp: Long = 0
): RealmObject() {
    companion object {
        const val SORT_KEY = "completionTimestamp"
    }
}