package com.greenears.tasktracker.Controllers

import com.greenears.tasktracker.Models.CompletedTask
import com.greenears.tasktracker.Models.PlannedTask
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.createObject
import io.realm.kotlin.where

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class DataStoreController() {

    private val _realm = Realm.getDefaultInstance()

    var timeDelta: Long = 0L

    fun createTask(title: String,
                   frequency: Long,
                   leadTime: Long = 0,
                   gracePeriod: Long = 0,
                   priority: Int = 1) {
        _realm.executeTransaction {
            val task = PlannedTask()
            task.title = title
            task.frequency = frequency
            task.leadTime = leadTime
            task.gracePeriod = gracePeriod
            task.priority = priority
            _realm.copyToRealm(task)
        }
    }

    fun deleteTask(task: PlannedTask) {
        _realm.executeTransaction {
            task.completionHistory?.deleteAllFromRealm()
            task.deleteFromRealm()
        }
    }

    fun markTaskComplete(task: PlannedTask) {
        _realm.executeTransaction {
            val completedTask = _realm.createObject<CompletedTask>()
            completedTask.task = task
            completedTask.completionTimestamp = System.currentTimeMillis() + timeDelta
            _realm.copyToRealm(completedTask)

            task.lastCompletionTimestamp = completedTask.completionTimestamp
            _realm.copyToRealmOrUpdate(task)
        }
    }

    fun getTaskById(id: Long): PlannedTask? {
        return _realm.where<PlannedTask>()
                .equalTo(PlannedTask.PRIMARY_KEY, id)
                .findFirst()
    }

    fun getUpcomingTasks(): RealmResults<PlannedTask> {
        return _realm.where<PlannedTask>().findAll()
    }

    fun getTaskCompletionHistory(): RealmResults<CompletedTask> {
        return _realm.where<CompletedTask>().sort(CompletedTask.SORT_KEY, Sort.DESCENDING).findAll()
    }

    fun close() {
        _realm.close()
    }
}