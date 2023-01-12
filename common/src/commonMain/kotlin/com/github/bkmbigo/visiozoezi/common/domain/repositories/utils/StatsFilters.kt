package com.github.bkmbigo.visiozoezi.common.domain.repositories.utils

import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

fun List<ExerciseStat>.getWeeklyStats(
    date: LocalDate,
    isStartDate: Boolean = true
): List<ExerciseStat> {

    val weekStart: Instant
    val weekEnd: Instant


    if (isStartDate) {
        weekStart = date.atStartOfDayIn(TimeZone.currentSystemDefault())
        weekEnd = date.plus(1, DateTimeUnit.WEEK).atStartOfDayIn(TimeZone.currentSystemDefault())
    } else {
        weekEnd = date.atStartOfDayIn(TimeZone.currentSystemDefault())
        weekStart = date.minus(1, DateTimeUnit.WEEK).atStartOfDayIn(TimeZone.currentSystemDefault())
    }

    return this.filter {
        it.time in weekStart..weekEnd
    }
}

fun List<ExerciseStat>.mapByDate(): Map<LocalDate, ExerciseStat> {
    return this.associateBy {
        it.time.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
}

fun List<ExerciseStat>.getDuration(): Duration {
    var duration = Duration.ZERO
    this.forEach {
        if (it is ExerciseStat.TimedStat) {
            duration += it.duration
        }
    }
    return duration
}