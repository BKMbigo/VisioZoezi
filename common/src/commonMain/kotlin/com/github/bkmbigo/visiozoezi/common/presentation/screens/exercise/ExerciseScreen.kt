package com.github.bkmbigo.visiozoezi.common.presentation.screens.exercise

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassificationResult
import com.github.bkmbigo.visiozoezi.common.ml.classifier.models.PoseClassificationModel
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.CameraCounter
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.CameraPanel
import com.github.bkmbigo.visiozoezi.common.presentation.components.exercise.ExerciseScreenState
import com.github.bkmbigo.visiozoezi.common.presentation.components.exercise.ExerciseTimer
import com.github.bkmbigo.visiozoezi.common.presentation.components.exercise.ExerciseTopCard
import com.github.bkmbigo.visiozoezi.common.presentation.components.image.ExerciseImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    exercise: Exercise,
    statsRepository: StatsRepository,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    startPoseDetectionOnLaunch: Boolean = false
) {
    val DEFAULT_SAVE_TIMEOUT = 5

    val coroutineScope = rememberCoroutineScope()

    // State Variables
    val timedDuration = remember { mutableStateOf(Duration.ZERO) }
    val isTimerCounting = remember { mutableStateOf(false) }

    val counterState = remember { mutableStateOf(0) }

    val exerciseStat = remember { mutableStateOf<ExerciseStat?>(null) }

    val showEnablePoseDetectionCard = remember {
        mutableStateOf(
            // Show if capable of poseClassification and startPoseDetectionOnLaunch is false
            PoseClassificationModel.getPoseClassificationModels(exercise.id).isNotEmpty()
                    && !startPoseDetectionOnLaunch
        )
    }

    val classificationResultState =
        remember { mutableStateOf<PoseClassificationResult>(PoseClassificationResult.NoResult) }

    val screenState = remember {
        mutableStateOf(
            if (startPoseDetectionOnLaunch) ExerciseScreenState.RepetitiveExerciseState
            else
                if (PoseClassificationModel.getPoseClassificationModels(exercise.id).isEmpty())
                    ExerciseScreenState.TimedExerciseState(false)
                else
                    ExerciseScreenState.TimedExerciseState(true)
        )
    }



    LaunchedEffect(timedDuration.value, isTimerCounting.value) {
        if (screenState.value is ExerciseScreenState.TimedExerciseState) {
            if (isTimerCounting.value) {
                delay(1000)
                timedDuration.value += (1).toDuration(DurationUnit.SECONDS)
                var timerRem =
                    (timedDuration.value / (DEFAULT_SAVE_TIMEOUT).toDuration(DurationUnit.SECONDS)).toInt()
                        .toDouble()
                timerRem =
                    timedDuration.value.toDouble(DurationUnit.SECONDS) - (timerRem * DEFAULT_SAVE_TIMEOUT)
                if (timerRem == 0.0) {
                    // Save every 20 seconds
                    if (exerciseStat.value == null) {
                        // Create new stat
                        exerciseStat.value = ExerciseStat.TimedStat(
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


    DisposableEffect(exerciseStat.value, isTimerCounting.value) {
        onDispose {
            when (screenState.value) {
                is ExerciseScreenState.TimedExerciseState -> {
                    if (timedDuration.value > Duration.ZERO) {
                        coroutineScope.launch {
                            if (exerciseStat.value != null) {
                                if (exerciseStat.value?.id == null) {
                                    val newStat =
                                        (exerciseStat.value!! as ExerciseStat.TimedStat).copy(
                                            duration = timedDuration.value
                                        )
                                    val id = statsRepository.addStat(newStat)
                                    exerciseStat.value =
                                        (exerciseStat.value!! as ExerciseStat.TimedStat).copy(id = id)
                                } else {
                                    val newStat =
                                        (exerciseStat.value!! as ExerciseStat.TimedStat).copy(
                                            duration = timedDuration.value
                                        )
                                    statsRepository.updateStat(newStat)
                                }
                            } else {
                                val newStat = ExerciseStat.TimedStat(
                                    id = null,
                                    exerciseId = exercise.id,
                                    time = Clock.System.now(),
                                    duration = timedDuration.value
                                )
                                exerciseStat.value = newStat
                                statsRepository.addStat(newStat)
                            }
                        }
                    }
                }

                is ExerciseScreenState.RepetitiveExerciseState -> {
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
                    IconButton(onClick = { navigateBack() }) {
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
            ExerciseTopCard(
                showEnablePoseDetectionCard,
                screenState,
                exercise,
                modifier = Modifier.fillMaxWidth().weight(0.2f, false)
            )
            Spacer(Modifier.height(4.dp))
            ExerciseImage(
                gifUrl = exercise.gifUrl,
                modifier = Modifier.weight(0.4f, true).align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(4.dp))

            Row(
                Modifier.fillMaxWidth().weight(0.3f)
                    .padding(4.dp)
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                when (screenState.value) {
                    ExerciseScreenState.RepetitiveExerciseState -> {
                        CameraPanel(
                            classificationResultsState = classificationResultState,
                            modifier = Modifier.weight(0.5f, false)
                        )
                        CameraCounter(
                            counterState,
                            classificationResult = classificationResultState,
                            modifier = Modifier.weight(0.5f).fillMaxHeight(),
                        )
                    }

                    is ExerciseScreenState.TimedExerciseState -> {
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
