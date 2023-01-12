package com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories

import com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers.toBodyPart
import com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers.toBodyPartDb
import com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers.toEquipment
import com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers.toEquipmentDb
import com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers.toExercise
import com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers.toExerciseDb
import com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers.toTargetMuscle
import com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers.toTargetMuscleDb
import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import com.github.bkmbigo.visiozoezi.common.data.repositories.DatabaseExerciseRepository
import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseExerciseRepositoryImpl(
    private val database: VisioZoeziDatabase
) : DatabaseExerciseRepository {
    override fun databaseIsFilled(): Boolean {
        return database.exerciseQueries.selectAll().executeAsList().isNotEmpty() &&
                database.equipmentQueries.selectAll().executeAsList().isNotEmpty() &&
                database.body_partQueries.selectAll().executeAsList().isNotEmpty() &&
                database.target_muscleQueries.selectAll().executeAsList().isNotEmpty()
    }

    override suspend fun getAllExercises(): Flow<List<Exercise>> {
        return database.exerciseQueries.selectAll().asFlow().mapToList()
            .map { exerciseList ->

                val equipmentMap = database.equipmentQueries.selectAll().executeAsList()
                    .map { it.toEquipment() }
                    .associateBy { it.id.toInt() }

                val bodyPartMap = database.body_partQueries.selectAll().executeAsList()
                    .map { it.toBodyPart() }
                    .associateBy { it.id.toInt() }

                val targetMuscleMap = database.target_muscleQueries.selectAll().executeAsList()
                    .map { it.toTargetMuscle() }
                    .associateBy { it.id.toInt() }

                exerciseList.map { exercise ->
                    exercise.toExercise(
                        equipmentList = equipmentMap,
                        bodyPartList = bodyPartMap,
                        targetMuscleList = targetMuscleMap
                    )
                }
            }
    }

    override suspend fun searchExercise(query: String): List<Exercise> {

        val equipmentMap = database.equipmentQueries.selectAll().executeAsList()
            .map { it.toEquipment() }
            .associateBy { it.id.toInt() }

        val bodyPartMap = database.body_partQueries.selectAll().executeAsList()
            .map { it.toBodyPart() }
            .associateBy { it.id.toInt() }

        val targetMuscleMap = database.target_muscleQueries.selectAll().executeAsList()
            .map { it.toTargetMuscle() }
            .associateBy { it.id.toInt() }

        return database.exerciseQueries.searchByName(query).executeAsList()
            .map { exercise ->
                exercise.toExercise(
                    equipmentList = equipmentMap,
                    bodyPartList = bodyPartMap,
                    targetMuscleList = targetMuscleMap
                )
            }
    }

    override suspend fun getAllEquipment(): Flow<List<Equipment>> {
        return database.equipmentQueries.selectAll().asFlow().mapToList()
            .map { it.map { equipment -> equipment.toEquipment() } }
    }

    override suspend fun getAllBodyParts(): Flow<List<BodyPart>> {
        return database.body_partQueries.selectAll().asFlow().mapToList()
            .map { it.map { bodyPart -> bodyPart.toBodyPart() } }
    }

    override suspend fun getAllTargetMuscles(): Flow<List<TargetMuscle>> {
        return database.target_muscleQueries.selectAll().asFlow().mapToList()
            .map {
                it.map { targetMuscle ->
                    targetMuscle.toTargetMuscle()
                }
            }
    }

    override suspend fun insertExercises(exercises: List<Exercise>) {
        database.exerciseQueries.transaction {
            exercises.forEach { exercise ->
                database.exerciseQueries.insert(exercise.toExerciseDb())
            }
        }
    }

    override suspend fun insertEquipment(equipment: List<Equipment>) {
        database.equipmentQueries.transaction {
            equipment.forEach { equipment ->
                database.equipmentQueries.insert(equipment.toEquipmentDb())
            }
        }
    }

    override suspend fun insertBodyParts(bodyParts: List<BodyPart>) {
        database.body_partQueries.transaction {
            bodyParts.forEach { bodyPart ->
                database.body_partQueries.insert(bodyPart.toBodyPartDb())
            }
        }
    }

    override suspend fun insertTargetMuscle(targetMuscles: List<TargetMuscle>) {
        database.target_muscleQueries.transaction {
            targetMuscles.forEach { targetMuscle ->
                database.target_muscleQueries.insert(targetMuscle.toTargetMuscleDb())
            }
        }
    }
}