package com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.bkmbigo.visiozoezi.common.data.persistence.Exercise_stat
import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class StatsRepositoryImpl(
    private val database: VisioZoeziDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Main
) : StatsRepository {
    override suspend fun addStat(exerciseStat: ExerciseStat): Long {
        when (exerciseStat) {
            is ExerciseStat.TimedStat -> {
                database.exercise_statQueries.insertTimedStat(
                    null,
                    exerciseStat.exerciseId,
                    exerciseStat.time.epochSeconds,
                    exerciseStat.duration.toLong(DurationUnit.SECONDS)
                )
            }

            is ExerciseStat.RepetitiveStat -> {
                database.exercise_statQueries.insertRepetitiveStat(
                    null,
                    exerciseStat.exerciseId,
                    exerciseStat.time.epochSeconds,
                    exerciseStat.iterations
                )
            }
        }

        return database.exercise_statQueries.lastInsertRowId().executeAsOne()
    }

    override suspend fun updateStat(exerciseStat: ExerciseStat) {
        when (exerciseStat) {
            is ExerciseStat.TimedStat -> {
                database.exercise_statQueries.updateTimedStat(
                    exerciseStat.duration.toLong(
                        DurationUnit.SECONDS
                    ), exerciseStat.id!!
                )
            }

            is ExerciseStat.RepetitiveStat -> {
                database.exercise_statQueries.updateRepetitiveStat(
                    exerciseStat.iterations,
                    exerciseStat.id!!
                )
            }
        }
    }

    override suspend fun deleteStat(exerciseStat: ExerciseStat) {
        database.exercise_statQueries.deleteStat(exerciseStat.id!!)
    }

    override suspend fun deleteAllStats() {
        database.exercise_statQueries.deleteAll()
    }

    override suspend fun getAllStats(): Flow<List<ExerciseStat>> {
        return database.exercise_statQueries.selectAll().asFlow().mapToList(ioDispatcher)
            .map { it.map { stat -> stat.toExerciseStat() } }
    }

    private fun Exercise_stat.toExerciseStat(): ExerciseStat {
        return if (is_timed) {
            ExerciseStat.TimedStat(
                id,
                exercise_id,
                Instant.fromEpochSeconds(time),
                interval.toDuration(DurationUnit.SECONDS)
            )
        } else {
            ExerciseStat.RepetitiveStat(
                id,
                exercise_id,
                Instant.fromEpochSeconds(time),
                repetitions
            )
        }
    }
}