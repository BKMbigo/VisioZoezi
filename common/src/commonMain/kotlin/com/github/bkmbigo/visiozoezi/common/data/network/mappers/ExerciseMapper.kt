package com.github.bkmbigo.visiozoezi.common.data.network.mappers

import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.data.network.dto.ExerciseDto
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle


fun ExerciseDto.toExercise(
    equipmentList: List<Equipment>,
    bodyPartList: List<BodyPart>,
    targetMuscleList: List<TargetMuscle>
): Exercise {
    return Exercise(
        id = id.toLong(),
        name = name,
        gifUrl = gifUrl,
        equipment = equipmentList.filter { it.name == equipment }[0],
        bodyPart = bodyPartList.filter { it.name == bodyPart }[0],
        targetMuscle = targetMuscleList.filter { it.name == targetMuscle }[0]
    )
}