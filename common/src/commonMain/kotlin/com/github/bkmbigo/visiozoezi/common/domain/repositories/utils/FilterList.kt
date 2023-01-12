package com.github.bkmbigo.visiozoezi.common.domain.repositories.utils

import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle

fun List<Exercise>.filterList(
    equipment: Equipment?,
    bodyPart: BodyPart?,
    targetMuscle: TargetMuscle?
): List<Exercise> =
    this.filter {
        (if (equipment != null) it.equipment == equipment else true) &&
                (if (bodyPart != null) it.bodyPart == bodyPart else true) &&
                (if (targetMuscle != null) it.targetMuscle == targetMuscle else true)
    }