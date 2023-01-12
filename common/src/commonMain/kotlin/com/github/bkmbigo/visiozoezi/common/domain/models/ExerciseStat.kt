package com.github.bkmbigo.visiozoezi.common.domain.models

import kotlinx.datetime.Instant
import kotlin.time.Duration

sealed class ExerciseStat(
    open val id: Long? = null,
    open val exerciseId: Long,
    open val time: Instant,
) {
    data class TimedStat(
        override val id: Long? = null,
        override val exerciseId: Long,
        override val time: Instant,
        val duration: Duration
    ): ExerciseStat(id, exerciseId, time)
    data class RepetitiveStat(
        override val id: Long? = null,
        override val exerciseId: Long,
        override val time: Instant,
        val iterations: Int
    ): ExerciseStat(id, exerciseId, time)
}