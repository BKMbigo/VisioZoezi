package com.github.bkmbigo.visiozoezi.common.data.network.repositories

import com.github.bkmbigo.visiozoezi.common.data.network.dto.ExerciseDto
import com.github.bkmbigo.visiozoezi.common.data.network.api.ExerciseDbApi
import com.github.bkmbigo.visiozoezi.common.data.network.mappers.toExercise
import com.github.bkmbigo.visiozoezi.common.data.network.utils.logLogger
import com.github.bkmbigo.visiozoezi.common.data.network.utils.safeApiCall
import com.github.bkmbigo.visiozoezi.common.data.network.utils.toElements
import com.github.bkmbigo.visiozoezi.common.data.repositories.NetworkExerciseRepository
import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import io.ktor.client.call.body

class NetworkExerciseRepositoryImpl(
    private val exerciseDbApi: ExerciseDbApi = ExerciseDbApi()
) : NetworkExerciseRepository {
    override suspend fun getAllExercises(
        equipmentList: List<Equipment>,
        bodyPartList: List<BodyPart>,
        targetMuscleList: List<TargetMuscle>
    ): List<Exercise> =
        safeApiCall {
            exerciseDbApi.getAllExercises()
                .body<List<ExerciseDto>>()
                .map { it.toExercise(equipmentList, bodyPartList, targetMuscleList) }
        }.toElements()

    override suspend fun getAllEquipment(): List<Equipment> =
        safeApiCall {
            exerciseDbApi.getAllEquipment().body<List<String>>()
                .mapIndexed { index, name -> Equipment(index.toLong(), name) }
        }.toElements()

    override suspend fun getAllBodyParts(): List<BodyPart> =
        safeApiCall {
            exerciseDbApi.getAllBodyPart().body<List<String>>()
                .mapIndexed { index, name -> BodyPart(index.toLong(), name) }
        }.toElements()


    override suspend fun getAllTargetMuscles(): List<TargetMuscle> =
        safeApiCall {
            exerciseDbApi.getAllTargetMuscles().body<List<String>>()
                .mapIndexed { index, name -> TargetMuscle(index.toLong(), name) }
        }.toElements()
}