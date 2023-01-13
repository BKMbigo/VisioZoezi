package com.github.bkmbigo.visiozoezi.common.presentation.components.exercise

sealed class ExerciseScreenState {
    object RepetitiveExerciseState : ExerciseScreenState()
    data class TimedExerciseState(val isPoseDetectionCapable: Boolean) : ExerciseScreenState()
}
