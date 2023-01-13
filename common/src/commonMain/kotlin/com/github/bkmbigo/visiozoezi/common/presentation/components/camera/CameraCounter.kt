package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassificationResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CameraCounter(
    counterState: MutableState<Int>,
    classificationResult: State<PoseClassificationResult>,
    modifier: Modifier = Modifier,
) {
    var saveJob: Job? = null
    fun resetSaveJob() {
        saveJob = null
    }
    LaunchedEffect(classificationResult.value) {
        if (classificationResult.value is PoseClassificationResult.WithResult) {
            if ((classificationResult.value as PoseClassificationResult.WithResult).data) {
                if (saveJob == null) {
                    counterState.value = counterState.value + 1
                    saveJob = launch {
                        delay(500)
                        resetSaveJob()
                    }
                }
            }
        }
    }
    Box(modifier) {
        Text(
            text = "Counter: ${counterState.value}",
            fontSize = 19.sp,
            maxLines = 1,
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = {
                counterState.value = 0
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Reset Counter")
        }
    }
}