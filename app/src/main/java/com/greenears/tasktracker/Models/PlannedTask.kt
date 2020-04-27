package com.greenears.tasktracker.Models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

open class PlannedTask(
        @PrimaryKey
        var id: String = UUID.randomUUID().toString(),
        var title: String = "",
        var frequency: Long = 0,
        var leadTime: Long = 0,
        var gracePeriod: Long = 0,
        var priority: Int = 1,
        var lastCompletionTimestamp: Long = 0
): RealmObject(), Comparable<PlannedTask> {
    companion object {
        const val PRIMARY_KEY = "id"
    }

    @LinkingObjects("task")
    val completionHistory: RealmResults<CompletedTask>? = null

    fun getSortValue(): Double {
        lastCompletionTimestamp.also { lastCompletion ->
            val timeSinceCompletion = System.currentTimeMillis() - lastCompletion
            val timeUntilDue = frequency - timeSinceCompletion
            val timeOverdue = 0 - timeUntilDue

            /**
             * How urgent is this task? The tiers are:
             * - Not yet within the lead time (if any)
             * - Within lead time but not yet due
             * - Overdue but within the grace period (if any)
             * - Overdue beyond the grace period
             *
             * The relative urgency of a task is its progression through its tier
             * multiplied by its priority.
             */

            val relativeUrgency = when {
                timeUntilDue > leadTime -> {
                    timeUntilDue.toDouble() / (frequency - leadTime).toDouble()
                }
                timeUntilDue > 0 && timeUntilDue < leadTime -> {
                    val tierBonus = 5
                    tierBonus + (timeUntilDue.toDouble() / leadTime.toDouble())
                }
                timeUntilDue < 0 && timeOverdue < gracePeriod -> {
                    val tierBonus = 10
                    tierBonus + (timeOverdue.toDouble() / gracePeriod.toDouble())
                }
                else -> {
                    val tierBonus = 15
                    tierBonus + (timeOverdue.toDouble() /
                            (frequency - gracePeriod).toDouble())
                }
            }

            val prioritizedUrgency = relativeUrgency * priority

            return prioritizedUrgency
        }

        return priority.toDouble()
    }

    override fun compareTo(other: PlannedTask): Int {
        return getSortValue().compareTo(other.getSortValue())
    }
}