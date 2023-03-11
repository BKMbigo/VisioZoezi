package com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers

import com.github.bkmbigo.visiozoezi.common.data.network.dto.ExerciseDto
import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle

import com.github.bkmbigo.visiozoezi.common.data.persistence.Exercise as ExerciseDb
import com.github.bkmbigo.visiozoezi.common.data.persistence.Equipment as EquipmentDb
import com.github.bkmbigo.visiozoezi.common.data.persistence.Body_part as BodyPartDb
import com.github.bkmbigo.visiozoezi.common.data.persistence.Target_muscle as TargetMuscleDb

fun ExerciseDb.toExercise(
    equipmentList: Map<Int, Equipment>,
    bodyPartList: Map<Int, BodyPart>,
    targetMuscleList: Map<Int, TargetMuscle>
): Exercise{
    return Exercise(
        id = id,
        name = name,
        gifUrl = gif_url,
        equipment = equipmentList.getOrElse(equipment_id.toInt()) { equipmentList.values.first() },
        bodyPart = bodyPartList.getOrElse(body_part_id.toInt()) { bodyPartList.values.first() },
        targetMuscle = targetMuscleList.getOrElse(target_muscle_id.toInt()) { targetMuscleList.values.first() }
    )
}

fun Exercise.toExerciseDb(): ExerciseDb{
    return ExerciseDb(
        id = id,
        name = name,
        gif_url = gifUrl,
        equipment_id = equipment.id,
        body_part_id = bodyPart.id,
        target_muscle_id = targetMuscle.id
    )
}