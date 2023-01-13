package com.github.bkmbigo.visiozoezi.common.presentation.components.exercise

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise

@Composable
fun ExerciseTopCard(
    showEnablePoseDetection: MutableState<Boolean>,
    screenState: MutableState<ExerciseScreenState>,
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        if (showEnablePoseDetection.value) {
            if (constraints.maxHeight > 400.dp.value) {
                Column(modifier = Modifier.fillMaxSize()) {
                    if (showEnablePoseDetection.value) {
                        FullEnablePoseDetectionCard(
                            onAccept = {
                                screenState.value = ExerciseScreenState.RepetitiveExerciseState
                            },
                            onDismiss = {
                                showEnablePoseDetection.value = false
                            },
                            modifier = Modifier.fillMaxWidth().weight(0.5f).padding(4.dp)
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    FullExerciseInfo(
                        exercise,
                        Modifier.fillMaxWidth().weight(0.5f, true).padding(4.dp)
                    )
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth()) {
                    VerticalExerciseInfo(
                        exercise = exercise,
                        modifier = Modifier.fillMaxHeight().weight(0.5f, true)
                    )
                    if (showEnablePoseDetection.value) {
                        VerticalEnablePoseDetectionCard(
                            onAccept = {
                                screenState.value = ExerciseScreenState.RepetitiveExerciseState
                            },
                            onDismiss = {
                                showEnablePoseDetection.value = false
                            },
                            modifier = Modifier.fillMaxHeight().weight(0.5f, false)
                        )
                    }
                }
            }
        } else {
            FullExerciseInfo(
                exercise,
                Modifier.fillMaxWidth()
            )
        }
    }
}