package com.github.bkmbigo.visiozoezi.common.data.repositories

import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import kotlinx.coroutines.flow.Flow

interface DatabaseExerciseRepository {

    suspend fun databaseIsFilled(): Boolean

    suspend fun getAllExercises(): Flow<List<Exercise>>

    suspend fun searchExercise(query: String): List<Exercise>

    suspend fun getAllEquipment(): Flow<List<Equipment>>

    suspend fun getAllBodyParts(): Flow<List<BodyPart>>

    suspend fun getAllTargetMuscles(): Flow<List<TargetMuscle>>

    suspend fun insertExercises(exercises: List<Exercise>)

    suspend fun insertEquipment(equipment: List<Equipment>)

    suspend fun insertBodyParts(bodyParts: List<BodyPart>)

    suspend fun insertTargetMuscle(targetMuscles: List<TargetMuscle>)

}