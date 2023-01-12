package com.github.bkmbigo.visiozoezi.common.domain.repositories

import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun searchExercise(
        query: String? = "",
        equipment: Equipment? = null,
        bodyPart: BodyPart? = null,
        targetMuscle: TargetMuscle? = null
    ): List<Exercise>

    suspend fun getAllExercises(): Flow<List<Exercise>>
    suspend fun getAllEquipment(): Flow<List<Equipment>>
    suspend fun getAllBodyParts(): Flow<List<BodyPart>>
    suspend fun getAllTargetMuscle(): Flow<List<TargetMuscle>>

}