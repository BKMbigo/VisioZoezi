package com.github.bkmbigo.visiozoezi.common.presentation.components.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseTimer(
    timerState: MutableState<Duration>,
    isTimerCounting: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {

    val progressIndicator1State = remember { mutableStateOf(false) }
    val progressIndicator2State = remember { mutableStateOf(false) }

    LaunchedEffect(isTimerCounting.value) {
        if (isTimerCounting.value) {
            progressIndicator1State.value = true
            delay(300)
            progressIndicator2State.value = true
        } else {
            progressIndicator1State.value = false
            progressIndicator2State.value = false
        }
    }

    Box(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            if (progressIndicator1State.value)
                LinearProgressIndicator(
                    color = Color.Blue,
                    trackColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                )
        }
        Box(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)) {
            if (progressIndicator2State.value)
                LinearProgressIndicator(
                    color = Color.Cyan,
                    trackColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                )
        }
        Box(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = timerState.value.toString(),
                fontSize = 21.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (isTimerCounting.value) {
                OutlinedButton(
                    onClick = {
                        timerState.value = Duration.ZERO
                        isTimerCounting.value = false
                    }
                ) {
                    Text("Reset Timer")
                }
            }
            if (!isTimerCounting.value) {
                Button(
                    onClick = { isTimerCounting.value = true },
                    enabled = !isTimerCounting.value
                ) {
                    Text("Start Timer")
                }
            }
        }


    }
}