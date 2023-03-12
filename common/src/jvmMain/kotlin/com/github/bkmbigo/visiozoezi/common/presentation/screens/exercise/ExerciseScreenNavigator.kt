package com.github.bkmbigo.visiozoezi.common.presentation.screens.exercise

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository

class ExerciseScreenNavigator(
    private val exercise: Exercise,
    private val statsRepository: StatsRepository,
    private val startPoseDetectionOnLaunch: Boolean = false
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        ExerciseScreen(
            exercise,
            statsRepository,
            modifier = Modifier.fillMaxSize(),
            { navigator?.pop() },
            startPoseDetectionOnLaunch
        )

    }

}