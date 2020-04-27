package com.greenears.tasktracker.Helpers

import com.greenears.tasktracker.Models.ViewModels.TimeTravelViewModel

/**
 * TaskTracker
 * Copyright © 2020 Daniel Grenier.  All rights reserved.
 */

class TimeTextFormatter {
    companion object {
        fun timeTextForMilliseconds(milliseconds: Long, useLongForm: Boolean = false): String {
            var timeText = ""

            var timeAccountedFor = 0L

            var lastTimeUnitString: String? = null
            var componentCount = 0
            (milliseconds / TimeTravelViewModel.WEEK_MILLISECONDS)
                    .takeUnless { it == 0L }?.also { weeks ->
                        componentCount++
                        timeAccountedFor += (TimeTravelViewModel.WEEK_MILLISECONDS * weeks)
                        lastTimeUnitString = ""
                        lastTimeUnitString += "$weeks"
                        if (useLongForm) {
                            lastTimeUnitString += " week"

                            if (weeks > 1) {
                                lastTimeUnitString += "s"
                            }
                        } else {
                            lastTimeUnitString += "w"
                        }
                    }

            ((milliseconds - timeAccountedFor) / TimeTravelViewModel.DAY_MILLISECONDS)
                    .takeUnless { it == 0L }?.also { days ->
                        componentCount++
                        timeAccountedFor += (TimeTravelViewModel.DAY_MILLISECONDS * days)

                        if (lastTimeUnitString != null) {
                            if (useLongForm && timeText.length > 0) {
                                timeText += ","
                            }
                            timeText += lastTimeUnitString
                        }
                        lastTimeUnitString = ""

                        if (timeText.length > 0) {
                            lastTimeUnitString += " "
                        }

                        lastTimeUnitString += "$days"
                        if (useLongForm) {
                            lastTimeUnitString += " day"

                            if (days > 1) {
                                lastTimeUnitString += "s"
                            }
                        } else {
                            lastTimeUnitString += "d"
                        }
                    }

            ((milliseconds - timeAccountedFor) / TimeTravelViewModel.HOUR_MILLISECONDS)
                    .takeUnless { it == 0L }?.also { hours ->
                        componentCount++
                        timeAccountedFor += (TimeTravelViewModel.HOUR_MILLISECONDS * hours)

                        if (lastTimeUnitString != null) {
                            if (useLongForm && timeText.length > 0) {
                                timeText += ","
                            }
                            timeText += lastTimeUnitString
                        }
                        lastTimeUnitString = ""

                        if (timeText.length > 0) {
                            lastTimeUnitString += " "
                        }

                        lastTimeUnitString += "$hours"
                        if (useLongForm) {
                            lastTimeUnitString += " hour"

                            if (hours > 1) {
                                lastTimeUnitString += "s"
                            }
                        } else {
                            lastTimeUnitString += "h"
                        }
                    }

            ((milliseconds - timeAccountedFor) / TimeTravelViewModel.MINUTE_MILLISECONDS)
                    .takeUnless { it == 0L }?.also { minutes ->
                        componentCount++
                        if (lastTimeUnitString != null) {
                            if (useLongForm && timeText.length > 0) {
                                timeText += ","
                            }
                            timeText += lastTimeUnitString
                        }
                        lastTimeUnitString = ""

                        if (timeText.length > 0) {
                            lastTimeUnitString += " "
                        }

                        lastTimeUnitString += "$minutes"
                        if (useLongForm) {
                            lastTimeUnitString += " minute"

                            if (minutes > 1) {
                                lastTimeUnitString += "s"
                            }
                        } else {
                            lastTimeUnitString += "m"
                        }
                    }


            if (lastTimeUnitString != null && useLongForm && timeText.length > 0) {
                if (componentCount > 2) {
                    timeText += ","
                }
                timeText += " and"
            }

            timeText += lastTimeUnitString ?: "moments"

            return timeText
        }
    }
}