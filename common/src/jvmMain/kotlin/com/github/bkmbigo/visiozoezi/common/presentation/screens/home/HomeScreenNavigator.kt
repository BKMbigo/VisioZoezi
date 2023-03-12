package com.github.bkmbigo.visiozoezi.common.presentation.screens.home

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import com.github.bkmbigo.visiozoezi.common.presentation.screens.exercise.ExerciseScreenNavigator
import com.github.bkmbigo.visiozoezi.common.presentation.screens.filter.FilterScreenNavigator
import com.github.bkmbigo.visiozoezi.common.presentation.screens.user.UserScreenNavigator
import kotlinx.coroutines.Dispatchers

class HomeScreenNavigator(
    private val exerciseRepository: ExerciseRepository,
    private val statsRepository: StatsRepository
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val coroutineScope = rememberCoroutineScope()
        val screenModel =
            rememberScreenModel { HomeScreenModel(exerciseRepository, coroutineScope) }

        val fullExerciseList by screenModel.exerciseList.collectAsState(emptyList())

        HomeScreen(
            exerciseRepository = exerciseRepository,
            fullExerciseList = fullExerciseList,
            modifier = Modifier,
            navigateToExerciseScreen = { exercise: Exercise, startPoseDetection: Boolean ->
                navigator?.push(
                    ExerciseScreenNavigator(
                        exercise,
                        statsRepository,
                        startPoseDetection
                    )
                )
            },
            navigateToUserScreen = {
                navigator?.push(UserScreenNavigator(statsRepository))
            },
            navigateToFilterScreen = {
                navigator?.push(
                    FilterScreenNavigator(exerciseRepository, statsRepository)
                )
            },
            ioDispatcher = Dispatchers.IO
        )
    }


}