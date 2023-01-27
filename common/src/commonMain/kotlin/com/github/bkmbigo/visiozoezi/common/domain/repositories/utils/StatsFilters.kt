package com.github.bkmbigo.visiozoezi.common.domain.repositories.utils

import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import kotlinx.datetime.Clock
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
    date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    isStartDate: Boolean = false
): List<ExerciseStat> {

    val weekStart: Instant
    val weekEnd: Instant


    if (isStartDate) {
        weekStart = date.atStartOfDayIn(TimeZone.currentSystemDefault())
        weekEnd = date.plus(1, DateTimeUnit.WEEK).plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(TimeZone.currentSystemDefault())
    } else {
        weekEnd = date.plus(1, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.currentSystemDefault())
        weekStart = date.minus(1, DateTimeUnit.WEEK).plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(TimeZone.currentSystemDefault())
    }

    return this.filter {
        it.time in weekStart..weekEnd
    }
}

fun List<ExerciseStat>.getMonthlyStats(
    date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
): List<ExerciseStat> {

    val monthStart: Instant =
        LocalDate(date.year, date.month, 1).atStartOfDayIn(TimeZone.currentSystemDefault())
    val newDate = LocalDate(date.year, date.month, date.dayOfMonth)
    val monthEnd: Instant =
        newDate.minus(newDate.dayOfMonth - 1, DateTimeUnit.DAY).plus(1, DateTimeUnit.MONTH)
            .minus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(TimeZone.currentSystemDefault())

    return this.filter {
        it.time in monthStart..monthEnd
    }
}

fun List<ExerciseStat>.associateByDate(): Map<LocalDate, List<ExerciseStat>> {
    val dates = this.map { it.time.toLocalDateTime(TimeZone.currentSystemDefault()).date }
    val dateSet = dates.distinct()
    return dateSet.associateWith { date ->
        this.filter { it.time.toLocalDateTime(TimeZone.currentSystemDefault()).date == date }
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