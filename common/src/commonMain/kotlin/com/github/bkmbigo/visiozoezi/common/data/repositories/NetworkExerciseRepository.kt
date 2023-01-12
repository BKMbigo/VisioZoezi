package com.github.bkmbigo.visiozoezi.common.data.repositories

import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import kotlinx.coroutines.flow.Flow

interface NetworkExerciseRepository {

    /**
     *
     */
    suspend fun getAllExercises(
        equipmentList: List<Equipment>,
        bodyPartList: List<BodyPart>,
        targetMuscleList: List<TargetMuscle>
    ): List<Exercise>

    suspend fun getAllEquipment(): List<Equipment>

    suspend fun getAllBodyParts(): List<BodyPart>

    suspend fun getAllTargetMuscles(): List<TargetMuscle>

}