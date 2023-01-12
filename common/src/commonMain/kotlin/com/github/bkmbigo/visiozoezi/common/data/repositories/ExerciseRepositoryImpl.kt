package com.github.bkmbigo.visiozoezi.common.data.repositories

import com.github.bkmbigo.visiozoezi.common.data.network.repositories.NetworkExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import com.github.bkmbigo.visiozoezi.common.domain.repositories.utils.filterList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ExerciseRepositoryImpl(
    private val databaseExerciseRepository: DatabaseExerciseRepository,
    private val networkExerciseRepository: NetworkExerciseRepository = NetworkExerciseRepositoryImpl()
) : ExerciseRepository {

    var lastNetworkCall: Instant? = null

    override suspend fun searchExercise(
        query: String?,
        equipment: Equipment?,
        bodyPart: BodyPart?,
        targetMuscle: TargetMuscle?
    ): List<Exercise> {
        refreshDatabaseIfNecessary()

        return databaseExerciseRepository.searchExercise(query ?: "")
            .filterList(equipment, bodyPart, targetMuscle)
    }

    override suspend fun getAllExercises(): Flow<List<Exercise>> {
        refreshDatabaseIfNecessary()

        return databaseExerciseRepository.getAllExercises()
    }

    override suspend fun getAllEquipment(): Flow<List<Equipment>> {
        refreshDatabaseIfNecessary()

        return databaseExerciseRepository.getAllEquipment()
    }

    override suspend fun getAllBodyParts(): Flow<List<BodyPart>> {
        refreshDatabaseIfNecessary()

        return databaseExerciseRepository.getAllBodyParts()
    }

    override suspend fun getAllTargetMuscle(): Flow<List<TargetMuscle>> {
        refreshDatabaseIfNecessary()

        return databaseExerciseRepository.getAllTargetMuscles()
    }

    private suspend fun refreshDatabaseIfNecessary() {
        if (!databaseExerciseRepository.databaseIsFilled()) {
            if (lastNetworkCall == null) {
                refreshDatabase()
            } else {
                if ((Clock.System.now() - lastNetworkCall!!) > DEFAULT_NETWORK_TIMEOUT.toDuration(
                        DurationUnit.MILLISECONDS
                    )
                ) {
                    refreshDatabase()
                }
            }
        }
    }

    private suspend fun refreshDatabase() {
        val equipmentList =
            withContext(Dispatchers.IO) { networkExerciseRepository.getAllEquipment() }
        val bodyPartList =
            withContext(Dispatchers.IO) { networkExerciseRepository.getAllBodyParts() }
        val targetMuscleList =
            withContext(Dispatchers.IO) { networkExerciseRepository.getAllTargetMuscles() }

        lastNetworkCall = Clock.System.now()

        databaseExerciseRepository.insertEquipment(equipmentList)
        databaseExerciseRepository.insertBodyParts(bodyPartList)
        databaseExerciseRepository.insertTargetMuscle(targetMuscleList)

        val exerciseList =
            withContext(Dispatchers.IO) {
                networkExerciseRepository.getAllExercises(
                    equipmentList = equipmentList,
                    bodyPartList = bodyPartList,
                    targetMuscleList = targetMuscleList
                )
            }

        databaseExerciseRepository.insertExercises(exerciseList)
    }

    companion object {
        const val DEFAULT_NETWORK_TIMEOUT = 5_000
    }
}