package com.github.bkmbigo.visiozoezi.common.domain.models

import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle

data class Exercise(
    val id: Long,
    val name: String,
    val gifUrl: String,
    val equipment: Equipment,
    val bodyPart: BodyPart,
    val targetMuscle: TargetMuscle
)