package com.github.bkmbigo.visiozoezi.common.presentation.screens.home

sealed class HomeScreenState {
    object RepetitiveExerciseState : HomeScreenState()
    data class TimedExerciseState(val isPoseDetectionEnabled: Boolean) : HomeScreenState()
}
