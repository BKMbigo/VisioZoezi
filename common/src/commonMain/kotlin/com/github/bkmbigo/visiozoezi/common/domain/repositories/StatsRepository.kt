package com.github.bkmbigo.visiozoezi.common.domain.repositories

import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    suspend fun addStat(exerciseStat: ExerciseStat): Long

    suspend fun updateStat(exerciseStat: ExerciseStat)

    suspend fun deleteStat(exerciseStat: ExerciseStat)

    suspend fun getAllStats(): Flow<List<ExerciseStat>>
}