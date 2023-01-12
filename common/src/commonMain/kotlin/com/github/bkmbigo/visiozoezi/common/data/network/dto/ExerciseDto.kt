package com.github.bkmbigo.visiozoezi.common.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    val id: Int,
    val name: String,
    val gifUrl: String,
    val equipment: String,
    val bodyPart: String,
    @SerialName("target")
    val targetMuscle: String
)
