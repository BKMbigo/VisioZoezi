package com.github.bkmbigo.visiozoezi.common.presentation.screens.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import com.github.bkmbigo.visiozoezi.common.ml.classifier.models.PoseClassificationModel
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.CameraCounter
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.CameraPanel
import com.github.bkmbigo.visiozoezi.common.presentation.components.exercise.EnablePoseDetectionCard
import com.github.bkmbigo.visiozoezi.common.presentation.components.exercise.ExerciseTimer
import com.github.bkmbigo.visiozoezi.common.presentation.components.image.ExerciseImage
import com.github.bkmbigo.visiozoezi.common.presentation.screens.home.HomeScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ExerciseScreen(
    private val exercise: Exercise,
    private val statsRepository: StatsRepository,
    private val startPoseDetectionOnLaunch: Boolean = false
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val coroutineScope = rememberCoroutineScope()

        // State Variables
        val timedDuration = remember { mutableStateOf<Duration>(Duration.ZERO) }
        val isTimerCounting = remember { mutableStateOf(false) }

        val counterState = remember { mutableStateOf(0) }

        val exerciseStat = remember { mutableStateOf<ExerciseStat?>(null) }

        val showEnablePoseDetectionCard = remember { mutableStateOf(startPoseDetectionOnLaunch) }

        val screenState = remember {
            mutableStateOf(
                if (startPoseDetectionOnLaunch) HomeScreenState.RepetitiveExerciseState
                else
                    if (PoseClassificationModel.getPoseClassificationModels(exercise.id).isEmpty())
                        HomeScreenState.TimedExerciseState(false)
                    else
                        HomeScreenState.TimedExerciseState(true)
            )
        }



        LaunchedEffect(timedDuration.value, isTimerCounting.value) {
            if (screenState.value is HomeScreenState.TimedExerciseState) {
                if (isTimerCounting.value) {
                    delay(1000)
                    timedDuration.value += (1).toDuration(DurationUnit.SECONDS)
                    if ((timedDuration.value / (20).toDuration(DurationUnit.SECONDS)) == 0.0) {
                        // Save every 20 seconds
                        if (exerciseStat.value == null) {
                            // Create new stat
                            ExerciseStat.TimedStat(
                                id = null,
                                exerciseId = exercise.id,
                                time = Clock.System.now(),
                                duration = timedDuration.value
                            )
                        } else {
                            exerciseStat.value =
                                (exerciseStat.value!! as ExerciseStat.TimedStat).copy(duration = timedDuration.value)
                        }
                    }
                }
            }
        }


        DisposableEffect(exerciseStat.value) {
            onDispose {
                when (screenState.value) {
                    is HomeScreenState.TimedExerciseState -> {
                        if (timedDuration.value > Duration.ZERO) {
                            coroutineScope.launch {
                                if (exerciseStat.value != null) {
                                    if (exerciseStat.value?.id == null) {
                                        val id = statsRepository.addStat(exerciseStat.value!!)
                                        exerciseStat.value =
                                            (exerciseStat.value!! as ExerciseStat.TimedStat).copy(id = id)
                                    } else {
                                        statsRepository.updateStat(exerciseStat.value!!)
                                    }
                                }
                            }
                        }
                    }

                    is HomeScreenState.RepetitiveExerciseState -> {
                        if (counterState.value > 0) {
                            coroutineScope.launch {
                                if (exerciseStat.value != null) {
                                    if (exerciseStat.value?.id == null) {
                                        val id = statsRepository.addStat(exerciseStat.value!!)
                                        exerciseStat.value =
                                            (exerciseStat.value!! as ExerciseStat.RepetitiveStat).copy(
                                                id = id
                                            )
                                    } else {
                                        statsRepository.updateStat(exerciseStat.value!!)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Exercise") },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth().weight(0.3f)) {
                    Card(
                        modifier = Modifier.weight(0.5f, true)
                            .padding(4.dp)
                            .padding(horizontal = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(6.dp)) {
                            Text(
                                text = exercise.name,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Divider(Modifier.padding(horizontal = 4.dp))
                            Spacer(Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Card(
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                    ),
                                    elevation = CardDefaults.elevatedCardElevation(
                                        defaultElevation = 4.dp
                                    ),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(6.dp)) {
                                        Text(
                                            text = "Equipment:",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = exercise.equipment.name,
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }

                                Card(
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                    ),
                                    elevation = CardDefaults.elevatedCardElevation(
                                        defaultElevation = 6.dp
                                    ),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(6.dp)) {
                                        Text(
                                            text = "Body Part:",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = exercise.bodyPart.name,
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }

                                Card(
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                    ),
                                    elevation = CardDefaults.elevatedCardElevation(
                                        defaultElevation = 6.dp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                ) {
                                    Column(modifier = Modifier.padding(4.dp)) {
                                        Text(
                                            text = "Target Muscle:",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = exercise.targetMuscle.name,
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (showEnablePoseDetectionCard.value) {
                        EnablePoseDetectionCard(
                            onAccept = { },
                            onDismiss = { showEnablePoseDetectionCard.value = false },
                            modifier = Modifier.padding(2.dp).weight(0.5f)
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                ExerciseImage(
                    gifUrl = exercise.gifUrl,
                    modifier = Modifier.weight(0.3f, true).align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    Modifier.fillMaxWidth().weight(0.4f)
                        .padding(4.dp)
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    when (screenState.value) {
                        HomeScreenState.RepetitiveExerciseState -> {
                            CameraPanel(
                                modifier = Modifier.weight(0.5f, false)
                            )
                            CameraCounter(
                                counterState,
                                modifier = Modifier.weight(0.5f).fillMaxHeight()
                            )
                        }

                        is HomeScreenState.TimedExerciseState -> {
                            ExerciseTimer(
                                timerState = timedDuration,
                                isTimerCounting = isTimerCounting,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

            }
        }


    }
}